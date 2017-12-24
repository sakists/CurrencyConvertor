package gr.tsiriath_android.currencyconvertor;

import android.provider.BaseColumns;

/**
 * Created by tsiriath on 24/12/2017.
 */

public final class RatesContract {

    public  class RatesEntry implements BaseColumns {

        public static final String TABLE_NAME = "rates";
        public static final String COL_DATE = "datetime";
        public static final String COL_BASECUR = "basecur";
        public static final String COL_JSONTEXT = "json_str";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_DATE + " TEXT PRIMARY KEY, " +
                COL_BASECUR + " TEXT, " +
                COL_JSONTEXT + " TEXT )";
    }

    // Empty constructor
    private RatesContract(){

    }

}
