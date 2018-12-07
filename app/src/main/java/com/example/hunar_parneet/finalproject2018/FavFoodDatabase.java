package com.example.hunar_parneet.finalproject2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavFoodDatabase extends SQLiteOpenHelper{



        protected static final String DATABASE_NAME = "FavouriteFood.db";
        protected static final String ACTIVITY_NAME = " FavFoodDatabase ";
        protected static final int  VERSION_NUM = 2 ;
        protected static final String KEY_Id = "id";
        protected static final String KEY_FOOD = "fav_food";
        protected static final String TABLE_NAME ="favFood_table";
        protected static final String KEY_CALORIE = "food_calories";
        protected static final String KEY_FAT = "food_fat";
        public static final String COLUMN_MESSAGE = "_msg";


        public FavFoodDatabase(Context ctx){

            super(ctx,DATABASE_NAME,null,VERSION_NUM );
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(" CREATE TABLE " + TABLE_NAME + "(" +
                    KEY_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FOOD + " TEXT, " + KEY_CALORIE + " TEXT, " + KEY_FAT + " TEXT);"
            );
            Log.i(ACTIVITY_NAME, "Calling onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
            onCreate(db);
            Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion +"newVersion=" + newVersion);
        }
    }

