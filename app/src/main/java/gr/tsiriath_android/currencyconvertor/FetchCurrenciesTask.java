package gr.tsiriath_android.currencyconvertor;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


class FetchCurrenciesTask extends AsyncTask<Activity, Void, String[]> {
    private Activity parentActivity;

     @Override
     protected String[] doInBackground(Activity... params) {

        parentActivity = params[0];
        return fetchCurrenciesData();
     }

    @Override
    protected void onPostExecute(String[] strings){
        DisplayData.renderMainScreen(strings,parentActivity,"URL");
        super.onPostExecute(strings);
    }

     private String[] fetchCurrenciesData() {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String currenciesJsonStr;

         String baseCur = MainActivity.pref_baseCur();
            try {
                // Construct the URL for the api.fixer.io query
                // https://api.fixer.io/latest
                URL url = new URL("https://api.fixer.io/latest?base=" + baseCur);

                // Create the request to api.fixer.io, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream != null) { // If stream is not empty
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line, cr;
                    cr = "\n";
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        line = line + cr;
                        buffer.append(line);
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }else {
                        String sourceFlag = "URL";
                        currenciesJsonStr = buffer.toString();
                        Log.i("FetchCurrenciesTask", currenciesJsonStr);
                        List<String> currenciesList = CurrenciesJsonParser.getCurrenciesFromJson(currenciesJsonStr, sourceFlag);
                        int strSize = currenciesList.size();    //Get size of list
                        String[] tmp = new String[strSize];     //create string array with that size
                        return currenciesList.toArray(tmp);
                    }
                }
                return null;

            } catch (IOException e) {
                Log.e("urlConnection", "inputStream=NULL ");
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            //String[] tmp = {"Cannot fetch data from ECB. Please check Internet connection."};
            //return tmp;
            return null;
        }

     public static boolean isNetworkAvailable(Context context) {
         ConnectivityManager connectivityManager
                 = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
         return activeNetworkInfo != null && activeNetworkInfo.isConnected();
     }
 }

