package com.example.hunar_parneet.finalproject2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsDatabaseHelper extends SQLiteOpenHelper {

    protected static final String ACTIVITY_NAME = "NewsDatabaseHelper";
    private static final String DATABASE_NAME = "cbcNewsdb.db";
    private static final int VERSION_NUM = 4;

    protected static final String TABLE_NAME = "cbcNewstable";
    protected static final String KEY_ID = "id";
    protected static final String KEY_URL = "url";
    protected static final String KEY_NEWS_TITLE = "newtitle";

    public NewsDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_URL + " TEXT, " +
                KEY_NEWS_TITLE + " TEXT);"
        );
        Log.i(ACTIVITY_NAME, "Calling onCreate()");
    }



    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpdate(), oldVersion=" + oldVersion + ", newVersion=" + newVersion);
    }
}
