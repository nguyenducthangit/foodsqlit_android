package com.nguyenducthang.ktrabuoi3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

public class FoodDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "foods.sqlite";
    public static final int DB_VERSION = 1;
    public static final String TBL_NAME = "Food";
    public static final String COL_CODE = "FoodCode";
    public static final String COL_NAME = "FoodName";
    public static final String COL_DESCRIPTION = "FoodDes";
    public static final String COL_PRICE = "FoodPrice";
    public static final String COL_IMAGE = "FoodImage";
    public static final String COL_CATE = "FoodCate";

    public FoodDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " ( " + COL_CODE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " VARCHAR(50), " + COL_DESCRIPTION + " VARCHAR(100), " + COL_PRICE + " REAL, " + COL_IMAGE + " BLOB, " +
                COL_CATE + " VARCHAR(50))";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    //SELECT
    public Cursor getData(String sql){
        try{
            SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(sql, null);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    //UPDATE, DELETE ...
    public boolean execSql(String sql){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //SEARCH

    public Cursor searchData(String input) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (input == null || input.isEmpty()){
            return db.query(TBL_NAME, null, null, null, null, null, null);
        }

        String selection = COL_NAME + " LIKE ? OR " + COL_PRICE + " LIKE ? OR " + COL_CATE + " LIKE ?";
        String[] selectionArgs = new String[]{
                "%" + input + "%",
                "%" + input + "%",
                "%" + input + "%"
        };

        /*if (name != null && !name.isEmpty()) {
            keyWord = COL_NAME + " LIKE ?";
            selectionArgs = new String[] { "%" + name + "%" };
        } else if (price != null) {
            keyWord = COL_PRICE + " <= ?";
            selectionArgs = new String[] {String.valueOf(price)};
        } else if (cate != null && !cate.isEmpty()) {
            keyWord = COL_CATE + " LIKE ?";
            selectionArgs = new String[] { "%" + cate + "%" };
        }*/


        Cursor cursor = db.query(TBL_NAME, null, selection, selectionArgs
                , null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        } else {
            return null;
        }
    }
    //INSERT
    public boolean insertData(String name, String des, Double price, byte[] photo, String cate){
        try{
            SQLiteDatabase db = getWritableDatabase();
            String sql = "INSERT INTO " + TBL_NAME + " VALUES(null, ?, ?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();

            statement.bindString(1, name);
            statement.bindString(2, des);
            statement.bindDouble(3, price);
            statement.bindBlob(4, photo);
            statement.bindString(5, cate);

            statement.executeInsert();
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateData(int id, String name, String des, Double price, byte[] photo, String cate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NAME, name);
        contentValues.put(COL_DESCRIPTION, des);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_IMAGE, photo);
        contentValues.put(COL_CATE, cate);

        int updateStatus = db.update(TBL_NAME, contentValues, COL_CODE + " = ?", new String[]{String.valueOf(id)});
        db.close();

        return updateStatus > 0;
    }

    public boolean deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        int delete = db.delete(TBL_NAME, COL_CODE + " = ?", new String[]{String.valueOf(id)});
        db.close();

        return delete > 0;
    }

    public int getNumberOfRows(){
        Cursor cursor = getData("SELECT * FROM " + TBL_NAME);
        int numberOfRows = cursor.getCount();
        cursor.close();

        return numberOfRows;
    }

    public void createSampleData(){
        if (getNumberOfRows() == 0){
            try{
                execSql("INSERT INTO " + TBL_NAME + " VALUES(null, 'Don', 'Đặc sản Quảng Ngãi', 20000.0, null, 'Mon chinh')");
                execSql("INSERT INTO " + TBL_NAME + " VALUES(null, 'Bún Bò', 'Mon yeu thich', 50000.0, null, 'Mon chinh')");
            }catch (Exception e){
                Log.e("Error", e.getMessage().toString());
            }
        }
    }
}
