package com.nguyenducthang.ktrabuoi3.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenducthang.ktrabuoi3.R;
import com.nguyenducthang.ktrabuoi3.model.FoodModel;

import java.util.List;

public class FoodAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<FoodModel> foodModelList;

    public FoodAdapter(Context context, int layout, List<FoodModel> foodModelList) {
        this.context = context;
        this.layout = layout;
        this.foodModelList = foodModelList;
    }

    @Override
    public int getCount() {
        return foodModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.txtFoodCode = convertView.findViewById(R.id.txtFoodCode);
            holder.txtFoodName = convertView.findViewById(R.id.txtFoodName);
            holder.txtFoodDes = convertView.findViewById(R.id.txtFoodDes);
            holder.txtFoodPrice = convertView.findViewById(R.id.txtFoodPrice);
            holder.txtFoodCate = convertView.findViewById(R.id.txtFoodCate);
            holder.imv_foodImage = convertView.findViewById(R.id.imv_Food);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        FoodModel food = foodModelList.get(position);
        holder.txtFoodCode.setText(String.valueOf(food.getFoodCode()));
        holder.txtFoodName.setText(food.getFoodName());
        holder.txtFoodDes.setText(food.getFoodDes());
        holder.txtFoodPrice.setText(String.format("%.0f đ", food.getFoodPrice()));
        holder.txtFoodCate.setText(food.getFoodCate());

        if (food.getFoodImage() != null){
            byte[] image = food.getFoodImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.imv_foodImage.setImageBitmap(bitmap);
        }

        return convertView;
    }

    public class ViewHolder{
        ImageView imv_foodImage;
        TextView txtFoodName, txtFoodDes, txtFoodPrice, txtFoodCate, txtFoodCode;
    }
}
