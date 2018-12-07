package com.example.hunar_parneet.finalproject2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 */
public class OctranspoDatabaseHelper extends SQLiteOpenHelper {

    /**
     *
      */
    protected static final String ACTIVITY_NAME = "OctranspoDatabaseHelper";
    /**
     *
     */
    private static String DATABASE_NAME = "BusRoutes.db";
    /**
     *
     */
    private static int VERSION_NUM = 1;
    /**
     *
     */
    public static final String TABLE_NAME = "OCTRANSPO";
    /**
     *
     */
    public static final String COL_STOP_NUMBER = "KEY_STOP_NUMBER";
    /**
     *
     */
    public static final String COL_ID = "KEY_ID";

    /**
     *
     * @param ctx
     */

    public OctranspoDatabaseHelper(Context ctx){

        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " ( " + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_STOP_NUMBER+" String);" );
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME); //delete current table
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onCreate, oldVersion = "+oldVersion+" newVersion = "+newVersion);
    }
}
