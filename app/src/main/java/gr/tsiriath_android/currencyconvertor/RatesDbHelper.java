package gr.tsiriath_android.currencyconvertor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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


}
