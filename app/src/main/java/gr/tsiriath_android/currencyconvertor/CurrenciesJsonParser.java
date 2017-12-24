package gr.tsiriath_android.currencyconvertor;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

class CurrenciesJsonParser {

    static List<String> getCurrenciesFromJson(String jsonString, String sourceFlag) {

        List<String> results = new ArrayList<>();
        String resline;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String curBase = jsonObject.getString("base");
            String curDate = jsonObject.getString("date");

            JSONObject currenciesList = jsonObject.getJSONObject("rates");
            Log.i("JSONParser- count",  sourceFlag + " " + Integer.toString(currenciesList.length()));
            Log.i("JSONParser- rates",  currenciesList.toString());
            resline = curBase + " - 1.000";
            results.add(resline);
            resline = currenciesList.toString().replace("\"","");   //convert to string and remove char \
            resline = resline.replace(":"," - ");   //replace ":" with " - "
            resline = resline.replace("{","");      //remove char {
            resline = resline.replace("}","");      //remove char }
            String[] CurArray = resline.split(","); // split string to array
            Collections.addAll(results, CurArray);
            resline = curBase + " is the base currency." +  " Last update: " + curDate;
            results.add(resline);
            Log.i("JSONParser- Status line", resline);

            if (sourceFlag=="URL"){ // If data comes from URL save to DB else ignore
                Context context = getApplicationContext();
                RatesDbHelper  db = new RatesDbHelper(context);
                int res = db.deleteData(curDate);
                Log.i("JSONParser- Del_DB", String.valueOf(res) + " record deleted.");
                if (db.insertData(curDate,curBase,jsonString)){
                    Log.i("JSONParser- Ins_DB", curBase + " , " + curDate);
                }
            }

        }catch(JSONException e){
            Log.e("CurrenciesJsonParser",e.getMessage());
        }
        return results;
    }

}
