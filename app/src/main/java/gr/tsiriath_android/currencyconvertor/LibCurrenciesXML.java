package gr.tsiriath_android.currencyconvertor;

import android.content.Context;

class LibCurrenciesXML {

    private final Context context;
    private final String [][] fullDetailArray;

    LibCurrenciesXML(Context myContext ){
        String[] XMLCurTableCopy,XMLCurTableLine;

        this.context = myContext;
        XMLCurTableCopy = context.getResources().getStringArray(R.array.XMLCurTable);    //Copy xml table to array;
        int arrayLen = XMLCurTableCopy.length;       //Get array size
        fullDetailArray = new String [arrayLen][3];
        for(int i=0;i<arrayLen ;i++) {
            XMLCurTableLine = XMLCurTableCopy[i].split(",");
            fullDetailArray[i] = XMLCurTableLine;
        }
        //Log.i("LibCurrenciesXML - Len ", Integer.toString(arrayLen));
    }


    Integer findImgID(String searchCur){
        Integer curImg;

        curImg=context.getResources().getIdentifier("_noflag" , "drawable", context.getPackageName());  //set initial value _noflag.png
        for(String[] rowFullDetailArray:fullDetailArray) {
            if (searchCur.equals(rowFullDetailArray[0]))
                return context.getResources().getIdentifier(rowFullDetailArray[1] , "drawable", context.getPackageName());
        }
        return curImg;
    }

    public String getCurDescr(String searchCur){
        String detailDescr="DESCR not found.!";

        for(String[] rowFullDetailArray:fullDetailArray){
            if (searchCur.equals(rowFullDetailArray[0]))    // If found return text
                return rowFullDetailArray[2];
        }
        return detailDescr;     // If NOT found return ""
    }


    String[][] getDetailXMLCurTable(){
        return fullDetailArray;
    }
}
