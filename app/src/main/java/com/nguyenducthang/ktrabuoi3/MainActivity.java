package com.nguyenducthang.ktrabuoi3;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenducthang.ktrabuoi3.adapter.FoodAdapter;
import com.nguyenducthang.ktrabuoi3.database.FoodDB;
import com.nguyenducthang.ktrabuoi3.databinding.ActivityMainBinding;
import com.nguyenducthang.ktrabuoi3.model.FoodModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static FoodDB db;
    FoodAdapter foodAdapter;
    ArrayList<FoodModel> foods;
    ArrayList<FoodModel> foodList;
    FoodModel selectedFood = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        openDb();
        loadData();
        addEvents();

        registerForContextMenu(binding.lvFoods);
    }

    private void openDb(){
        db = new FoodDB(MainActivity.this);
        db.createSampleData();
    }

    private void addEvents() {
        binding.lvFoods.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFood = (FoodModel) foodAdapter.getItem(position);
                return false;
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord = binding.searchFood.getText().toString();

                Cursor cursor = db.searchData(keyWord);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    foodList = new ArrayList<>();
                    while (!cursor.isAfterLast()){
                        foodList.add(new FoodModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                cursor.getDouble(3), cursor.getBlob(4), cursor.getString(5)));

                        cursor.moveToNext();
                    }
                    foodAdapter = new FoodAdapter(MainActivity.this, R.layout.activity_food, foodList);
                    binding.lvFoods.setAdapter(foodAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        foodAdapter = new FoodAdapter(this, R.layout.activity_food, getDataFromDb());
        binding.lvFoods.setAdapter(foodAdapter);
    }

    private List<FoodModel> getDataFromDb() {
        foods = new ArrayList<>();
        Cursor cursor = db.getData("SELECT * FROM " + FoodDB.TBL_NAME);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            foods.add(new FoodModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getDouble(3), cursor.getBlob(4), cursor.getString(5)));

            cursor.moveToNext();
        }
        cursor.close();
        return foods;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnAdd){
            Intent iAdd = new Intent(MainActivity.this, AddActivity.class);
            startActivity(iAdd);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnUpdate){
            Intent iUpdated = new Intent(MainActivity.this, UpdateActivity.class);

            if (selectedFood != null){
                iUpdated.putExtra("food", selectedFood);
                startActivity(iUpdated);
            }
        }else if (item.getItemId() == R.id.mnDelete){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirm delete !");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setMessage("Do you want to delete this food? '" + selectedFood.getFoodName() + "'?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean deleteRows = db.deleteData(selectedFood.getFoodCode());

                    if (deleteRows){
                        Toast.makeText(MainActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                    Toast.makeText(MainActivity.this, "Fail !", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            Dialog dialog = builder.create();
            dialog.show();
        }
        return super.onContextItemSelected(item);
    }
}
