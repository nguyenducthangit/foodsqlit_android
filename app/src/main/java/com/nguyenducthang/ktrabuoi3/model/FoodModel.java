package com.nguyenducthang.ktrabuoi3.model;

import java.io.Serializable;

public class FoodModel implements Serializable {
    private int foodCode;
    private String foodName, foodDes;
    private Double foodPrice;
    private byte[] foodImage;
    private String foodCate;

    public FoodModel(int foodCode, String foodName, String foodDes, Double foodPrice, byte[] foodImage, String foodCate) {
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.foodDes = foodDes;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.foodCate = foodCate;
    }

    public int getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(int foodCode) {
        this.foodCode = foodCode;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDes() {
        return foodDes;
    }

    public void setFoodDes(String foodDes) {
        this.foodDes = foodDes;
    }

    public String getFoodCate() {
        return foodCate;
    }

    public void setFoodCate(String foodCate) {
        this.foodCate = foodCate;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public byte[] getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(byte[] foodImage) {
        this.foodImage = foodImage;
    }
}
