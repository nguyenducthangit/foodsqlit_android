package com.nguyenducthang.ktrabuoi3;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import com.nguyenducthang.ktrabuoi3.database.FoodDB;
import com.nguyenducthang.ktrabuoi3.databinding.ActivityUpdateBinding;
import com.nguyenducthang.ktrabuoi3.model.FoodModel;

import java.io.ByteArrayOutputStream;

public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    ActivityResultLauncher<Intent> launcher;
    FoodModel food;
    int foodCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK && o.getData() != null){
                    Bitmap bitmap = (Bitmap) o.getData().getExtras().get("data");
                    binding.imvCap.setImageBitmap(bitmap);
                }
            }
        });

        getData();
        addEvents();

    }

    private void getData() {
        Intent intent = getIntent();
        food = (FoodModel) intent.getSerializableExtra("food");

        foodCode = food.getFoodCode();

        binding.edName.setText(food.getFoodName());
        binding.edDes.setText(food.getFoodDes());
        binding.edPrice.setText(String.valueOf(food.getFoodPrice()));
        binding.edCate.setText(food.getFoodCate());

        if(food.getFoodImage() != null){
            byte[] image = food.getFoodImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            binding.imvCap.setImageBitmap(bitmap);
        }
    }

    private void addEvents() {
        binding.btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(iCamera);
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edName.getText().toString();
                String des = binding.edDes.getText().toString();
                String cate = binding.edCate.getText().toString();
                Double price = Double.parseDouble(binding.edPrice.getText().toString());

                boolean updated = MainActivity.db.updateData(foodCode, name, des, price, convertPhoto(), cate);

                if (updated){
                    Toast.makeText(UpdateActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(UpdateActivity.this, "Fail !", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private byte[] convertPhoto(){
        BitmapDrawable drawable = (BitmapDrawable) binding.imvCap.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return outputStream.toByteArray();
    }
}