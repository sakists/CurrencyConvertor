package gr.tsiriath_android.currencyconvertor;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.PendingIntent.getActivity;

/**
 * Created by tsiriath on 26/10/2017.
 */

    public class FetchCurrenciesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            return fetchCurrenciesData();
        }

    @Override
    protected void onPostExecute(String[] strings) {
        if (strings!= null){
            List<String> newData = new ArrayList<>(Arrays.asList(strings));
            Log.i("onPostExecute", strings[0]);
            Log.i("onPostExecute", strings[31]);
/*
        ArrayAdapter adapter = new ArrayAdapter(this(),
                            R.layout.list_item_currencies,
                            R.id.list_item_currencies_textview,
                newData);
            /*currenciesListAdapter.clear();
            for(String forecast:strings){
                forecastListAdapter.add(forecast);

            }*/
        }
        super.onPostExecute(strings);
    }

        private String[] fetchCurrenciesData() {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String currenciesJsonStr = null;

            try {
                // Construct the URL for the api.fixer.io query
                // https://api.fixer.io/latest
                URL url = new URL("https://api.fixer.io/latest");

                // Create the request to api.fixer.io, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream != null) { // If stream is not empty
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                    }else {
                        currenciesJsonStr = buffer.toString();
                        Log.i("FetchCurrenciesTask", currenciesJsonStr);
                        List<String> currenciesList = CurrenciesJsonParser.getCurrenciesFromJson(currenciesJsonStr);
                        int strSize = currenciesList.size();    //Get size of list
                        String[] tmp = new String[strSize];     //create string array with that size
                        return currenciesList.toArray(tmp);
                    }
                }
                return null;

            } catch (IOException e) {
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
            return null;
        }
    }

