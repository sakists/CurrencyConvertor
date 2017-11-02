package gr.tsiriath_android.currencyconvertor;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


 class FetchCurrenciesTask extends AsyncTask<Activity, Void, String[]> {
    private Activity parentActivity;
    private Context myContext;
    private ArrayAdapter<String> currenciesListAdapter;
    private ArrayList<ItemData> MySpnCurList;
    private ListView currenciesListView;
    private TextView newWelcomeMessage;

    @Override
    protected String[] doInBackground(Activity... params) {

        parentActivity = params[0];
        myContext = parentActivity.getApplicationContext();
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
            currenciesListAdapter = new ArrayAdapter<>(
                    parentActivity,
                    R.layout.list_item_currencies,
                    R.id.list_item_currencies_textview,
                    Arrays.asList(strings));
            currenciesListView =  parentActivity.findViewById(R.id.listview_currencies);
            currenciesListView.setAdapter(currenciesListAdapter);

            MySpnCurList = updSpnCurList(strings);  //Create parametrical spinner table
            Spinner sp1=parentActivity.findViewById(R.id.spin_cur_1);
            Spinner sp2=parentActivity.findViewById(R.id.spin_cur_2);
            Integer oldSp1 =sp1.getSelectedItemPosition()+1;      // Save old sp1 item position
            Integer oldSp2 =sp2.getSelectedItemPosition()+1;      // Save old sp2 item position
            SpinnerAdapter adapter=new SpinnerAdapter(parentActivity,R.layout.spinner_item_currencies,R.id.spin_txt,MySpnCurList);
            sp1.setAdapter(adapter);
            sp2.setAdapter(adapter);
            sp1.setSelection(oldSp1);    // Restore sp1 itemPos
            sp2.setSelection(oldSp2);    // Restore sp2 itemPos
        }
        super.onPostExecute(strings);
    }


    private ArrayList<ItemData> updSpnCurList(String[] myStrings) {

        String curTxt;
        Integer curImg;

        ArrayList<ItemData> result =new ArrayList<>();
        for(String rowStrings:myStrings){       //For each sting in mystrings
            curTxt =  rowStrings.substring(0,3);    //Get currency text
            curImg =  (new LibCurrenciesXML(myContext)).findImgID(curTxt);  //find currency flag
            result.add(new ItemData(" - " +curTxt, curImg));    // Create a new line for spinner
        }
       return result;
    }

        private String[] fetchCurrenciesData() {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String currenciesJsonStr;

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
                        return null;
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

