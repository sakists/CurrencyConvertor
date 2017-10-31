package gr.tsiriath_android.currencyconvertor;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesJsonParser {

    public static List<String> getCurrenciesFromJson(String jsonString) {

        List<String> results = new ArrayList<>();
        String resline;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String curBase = jsonObject.getString("base");
            String curDate = jsonObject.getString("date");

            JSONObject currenciesList = jsonObject.getJSONObject("rates");
            Log.i("JSONParser- count",  Integer.toString(currenciesList.length()));
            resline = "EUR - 1.000";
            results.add(resline);
            resline = currenciesList.toString().replace("\"","");   //convert to string and remove char "
            resline = resline.replace(":"," - ");   //replace ":" with " - "
            resline = resline.replace("{","");      //remove char {
            resline = resline.replace("}","");      //remove char }
            String[] CurArray = resline.split(","); // split string to array
            for (int i=0; i <currenciesList.length();i++ ) {
                //Log.i("CurrenciesJSONParser", CurArray[i]);
                results.add(CurArray[i]);
            }
            resline = "Base currency is in " + curBase + ". Last update: " + curDate;
            results.add(resline);
            Log.i("JSONParser - Last line", resline);
        }catch(JSONException e){
            Log.e("CurrenciesJsonParser",e.getMessage());
        }
        return results;
    }

}
