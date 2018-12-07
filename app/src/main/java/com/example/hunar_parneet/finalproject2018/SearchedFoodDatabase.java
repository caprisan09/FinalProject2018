package com.example.hunar_parneet.finalproject2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SearchedFoodDatabase extends SQLiteOpenHelper {



        protected static final String DATABASE_NAME = "SearchedFood.db";
        protected static final String ACTIVITY_NAME = " SearchedFoodDatabase ";
        protected static final int  VERSION_NUM = 2 ;
        protected static final String KEY_Id = "id";
        protected static final String KEY_MESSAGE = "searched_food";
        protected static final String TABLE_NAME ="searchedFood_table";
        public static final String COLUMN_MESSAGE = "_msg";


        public SearchedFoodDatabase(Context ctx){

            super(ctx,DATABASE_NAME,null,VERSION_NUM );
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(" CREATE TABLE " + TABLE_NAME + "(" +
                    KEY_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_MESSAGE + " TEXT );"
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




