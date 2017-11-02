package gr.tsiriath_android.currencyconvertor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by tsiriath on 2/11/2017.
 */

public class LibCurrenciesXML {

    private Context context;

    public LibCurrenciesXML(Context myContext ){
        this.context = myContext;
    }

    public Integer getImgID(){
        Integer curImg;

        curImg=1;

        return  curImg;
    }

    public String[][] getDetailXMLCurTable(){
        String [][] result;
        String[] XMLCurTableCopy,XMLCurTableLine;

        XMLCurTableCopy = context.getResources().getStringArray(R.array.XMLCurTable);    //Copy xml table to array;
        int arrayLen = XMLCurTableCopy.length;       //Get array size
        result = new String [arrayLen][3];
        for(int i=0;i<arrayLen ;i++) {
            XMLCurTableLine = XMLCurTableCopy[i].split(",");
            result[i] = XMLCurTableLine;
        }
        Log.i("LibCurrenciesXML - Len ", Integer.toString(arrayLen));
        return result;
    }
}
