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
import java.util.List;

/**
 * Created by tsiriath on 26/10/2017.
 */

public class CurrenciesFragment extends Fragment{

    private ArrayAdapter<String> currenciesListAdapter;

    private String[] sampleData = {
            "AUD - 1.5282",
            "BGN - 1.9558",
            "BRL - 3.8134",
            "CAD - 1.4963",
            "CHF - 1.169",
            "CNY - 7.8317",
            "CZK - 25.589",
            "DKK - 7.4429"};

    public  String[] ReadSampleData(){
        return  sampleData;
    }

    public CurrenciesFragment (){
        // Required empty constractor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    public class FetchCurrenciesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return fetchCurrenciesData();
        }

        private String fetchCurrenciesData() {

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
                if (inputStream == null) {
                    // Nothing to do.
                }
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
                }
                currenciesJsonStr = buffer.toString();
                Log.i("ForecastFragment", currenciesJsonStr);
                // List<String> weatherList = WeatherJsonParser.getWeatherGromJson(currenciesJsonStr, 7);

                //String[] wAr = new String[7];
                //return weatherList.toArray(wAr);
                return currenciesJsonStr;

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
}
