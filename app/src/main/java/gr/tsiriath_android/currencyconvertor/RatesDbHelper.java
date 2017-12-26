package gr.tsiriath_android.currencyconvertor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tsiriath on 24/12/2017.
 */

public class RatesDbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "currencies.db";

    public RatesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public  void onCreate(SQLiteDatabase db) {
        db.execSQL(RatesContract.RatesEntry.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + RatesContract.RatesEntry.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public boolean insertData(String datetime, String basecur, String json_str) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RatesContract.RatesEntry.COL_DATE, datetime);
        values.put(RatesContract.RatesEntry.COL_BASECUR, basecur);
        values.put(RatesContract.RatesEntry.COL_JSONTEXT, json_str);

        long result = db.insert(RatesContract.RatesEntry.TABLE_NAME, null, values);
        db.close();

        if (result==-1){
            Log.i("DbInsertData - RESULT" , " NOT OK ");
             return false;
        }else{
            Log.i("DbInsertData - RESULT" , " !!! OK !!!");
             return true;
        }
    }

    public Cursor getLastData(){

        String tblName = RatesContract.RatesEntry.TABLE_NAME;   // Get table name
        String colDate = RatesContract.RatesEntry.COL_DATE;     // Get date column
        String strSQL = "Select * from " + tblName + " ORDER BY " +  colDate + " DESC LIMIT 1"; //Compose SQL query
        SQLiteDatabase db = this.getWritableDatabase();         // Open database
        Cursor res= db.rawQuery(strSQL,null);                   // Execute SQL query
//        res.moveToNext();
//        Log.i("DbGetData - StrSQL" , res.getString(0) + " - " + res.getString(1) + " - " + res.getString(2));
//        res.moveToFirst();
        return res;
    }

    public int deleteData(String recDateID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(RatesContract.RatesEntry.TABLE_NAME,"datetime=?",new String[] { recDateID});
    }

    public int deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(RatesContract.RatesEntry.TABLE_NAME, null, null);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, RatesContract.RatesEntry.TABLE_NAME);
    }

//    public  fetchLatestData(){
//
//    }
}
