package gr.tsiriath_android.currencyconvertor;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class FetchCurrenciesTask extends AsyncTask<Activity, Void, String[]> {
    private Activity parentActivity;
    private ArrayAdapter<String> currenciesListAdapter;
    private ListView currenciesListView;
    private TextView newWelcomeMessage;

    @Override
    protected String[] doInBackground(Activity... params) {

        parentActivity = params[0];
        return fetchCurrenciesData();
     }

    @Override
    protected void onPostExecute(String[] strings) {

        if (strings!= null){
            //Update welcome message. Its the array's last element
            newWelcomeMessage= parentActivity.findViewById(R.id.welcomeMessage);
            newWelcomeMessage.setText(strings[strings.length-1]);
            //Update listview with new values. Welcome message not included
            strings = Arrays.copyOfRange (strings,0,strings.length-1);
            currenciesListAdapter = new ArrayAdapter<String>(
                    parentActivity,
                    R.layout.list_item_currencies,
                    R.id.list_item_currencies_textview,
                    Arrays.asList(strings));
            currenciesListView =  parentActivity.findViewById(R.id.listview_currencies);
            currenciesListView.setAdapter(currenciesListAdapter);
        }
        super.onPostExecute(strings);
    }

        private String[] fetchCurrenciesData() {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String currenciesJsonStr = "";

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

