package gr.tsiriath_android.currencyconvertor;

import android.content.Context;
import android.util.Log;

/**
 * Created by tsiriath on 2/11/2017.
 */

public class LibCurrenciesXML {

    private Context context;
    private String [][] fullDetailArray;

    public LibCurrenciesXML(Context myContext ){
        String[] XMLCurTableCopy,XMLCurTableLine;

        this.context = myContext;
        XMLCurTableCopy = context.getResources().getStringArray(R.array.XMLCurTable);    //Copy xml table to array;
        int arrayLen = XMLCurTableCopy.length;       //Get array size
        fullDetailArray = new String [arrayLen][3];
        for(int i=0;i<arrayLen ;i++) {
            XMLCurTableLine = XMLCurTableCopy[i].split(",");
            fullDetailArray[i] = XMLCurTableLine;
        }
        Log.i("LibCurrenciesXML - Len ", Integer.toString(arrayLen));
    }


    public Integer findImgID(String searchCur){
        Integer curImg;

        curImg=context.getResources().getIdentifier("_noflag" , "drawable", context.getPackageName());  //set initial value _noflag.png
        int arrayLen = fullDetailArray.length;
        for (int i=0;i<arrayLen;i++) {
            if (searchCur.equals(fullDetailArray[i][0]))
                return context.getResources().getIdentifier(fullDetailArray[i][1] , "drawable", context.getPackageName());
        }
        return curImg;
    }

    public String getCurDescr(String searchCur){
        String detailDescr="";

        int arrayLen = fullDetailArray.length;      // Find array length
        for (int i=0;i<arrayLen;i++) {              // search array
            if (searchCur.equals(fullDetailArray[i][0]))    // If found return text
                return fullDetailArray[i][2];
        }
        return detailDescr;     // If NOT found return ""
    }


    public String[][] getDetailXMLCurTable(){

        return fullDetailArray;
    }
}
