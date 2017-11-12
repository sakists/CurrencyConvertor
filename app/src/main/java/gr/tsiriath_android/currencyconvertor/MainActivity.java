package gr.tsiriath_android.currencyconvertor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> currenciesListAdapter;
    private Spinner spnCur1,spnCur2;
    private static  String[] masterData = {
            "EUR - 1.0000",
            "Internet Connection NOT available.",
            "No history data on local Database.",
            "Please select Options menu => Refresh."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnSwitch,btnCalc,btnClear;
        ArrayList<ItemData> spnCurList;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create Both spinners with images and text.
        spnCurList = fillspnCurList();  //Create parametrical spinner table
        spnCur1=(Spinner)findViewById(R.id.spin_cur_1);
        spnCur2=(Spinner)findViewById(R.id.spin_cur_2);
        SpinnerAdapter adapter=new SpinnerAdapter(this, spnCurList);
        spnCur1.setAdapter(adapter);
        spnCur2.setAdapter(adapter);

        btnSwitch = (Button) findViewById(R.id.btn_switch); //σύνδεση του btnSwitch με το Button Switch
        btnCalc = (Button) findViewById(R.id.btn_calc);     //σύνδεση του btnCalc με το Button Calc
        btnClear = (Button) findViewById(R.id.btn_clear);   //σύνδεση του btnClear με το Button Clear

        btnSwitch.setOnClickListener(switchOnClickListener);//κλήση δημιουργίας listener για το Button Switch
        btnCalc.setOnClickListener(calcOnClickListener);    //κλήση δημιουργίας listener για το Button Calc
        btnClear.setOnClickListener(clearOnClickListener);  //κλήση δημιουργίας listener για το Button Clear

        createMyArrayAdapter(this, masterData);
        setMyListViewAdapter();

        if (FetchCurrenciesTask.isNetworkAvailable(getApplicationContext())){
            showToast(getApplicationContext(),getString(R.string.Try_to_update),Toast.LENGTH_SHORT);
            FetchCurrenciesTask task = new FetchCurrenciesTask();
            task.execute(this);
        }else {
            showToast(getApplicationContext(),getString(R.string.No_connection),Toast.LENGTH_LONG);
        }

    }

    private ArrayList<ItemData> fillspnCurList() {

        String[][] XMLCurTable;
        Integer curImg;

        ArrayList<ItemData> result =new ArrayList<>();
        XMLCurTable = (new LibCurrenciesXML(this)).getDetailXMLCurTable();   //Get full detail from XMLCurTable
        for(String[] rowXMLCurTable:XMLCurTable ){
            curImg = getResources().getIdentifier(rowXMLCurTable[1], "drawable", getPackageName());   // Convert image name to images's ID
            result.add(new ItemData(" - " + rowXMLCurTable[0]," - " + rowXMLCurTable[2], curImg));    // Create a new line for spinner

        }
        return result;
    }

    private void createMyArrayAdapter(Activity myActivity, String[] newMasterData){

        currenciesListAdapter = new ArrayAdapter<>(
                myActivity,
                R.layout.list_item_currencies,
                R.id.list_item_currencies_textview,
                Arrays.asList(newMasterData));
    }

    private void setMyListViewAdapter(){
        ListView currenciesListView;

        currenciesListView =  (ListView)findViewById(R.id.listview_currencies);
        currenciesListView.setAdapter(currenciesListAdapter);
        currenciesListView.setOnItemClickListener(listItemOnClickListener);
    }

    private final View.OnClickListener switchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnSwitchClicked();  //κλήση της btnClearClicked
        }
    };

    private final View.OnClickListener calcOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnCalcClicked();  //κλήση της btnCalcClicked
        }
    };

    private final View.OnClickListener clearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnClearClicked();  //κλήση της btnClearClicked
        }
    };

    private final AdapterView.OnItemClickListener listItemOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?>  parent, View v, int position, long id) {
            listItemClicked(position);  //κλήση της listItemClicked
        }
    };

    private void listItemClicked(int position) {    // Υλοποίηση της listItemClicked

        Context context = getApplicationContext();
        String text = toastCalcCurrency(position);
        int duration = Toast.LENGTH_LONG;
        showToast(context, text, duration);
    }

    private void btnSwitchClicked() {    // Υλοποίηση της btnSwitchClicked

        spnCur1=(Spinner)findViewById(R.id.spin_cur_1);
        spnCur2=(Spinner)findViewById(R.id.spin_cur_2);
        Integer oldSp1 =spnCur1.getSelectedItemPosition();          // Save old sp1 item position
        spnCur1.setSelection(spnCur2.getSelectedItemPosition());    // Change sp1 itemPos with sp2 itemPos
        spnCur2.setSelection(oldSp1);                               // Change sp2 with old sp1 itemPos
    }

    private void btnCalcClicked() {    // Υλοποίηση της btnCalcClicked
        Double value;
        String cur1,cur2,resTxt;
        EditText edTxtCur1,edTxtCur2;

        edTxtCur1=(EditText) findViewById(R.id.edt_cur_1);
        if (!edTxtCur1.getText().toString().equals("")) {
            value = Double.valueOf(edTxtCur1.getText().toString());
            spnCur1=(Spinner)findViewById(R.id.spin_cur_1);     //Get 1st spinner object
            spnCur2=(Spinner)findViewById(R.id.spin_cur_2);     //Get 2bd spinner object
            TextView textView1 = spnCur1.getSelectedView().findViewById(R.id.spin_txt);     //Get TextView from 1st spinner object
            cur1 = textView1.getText().toString().substring(3);                             //Get string from TextView of 1st spinner
            TextView textView2 = spnCur2.getSelectedView().findViewById(R.id.spin_txt);     //Get TextView from 2nd spinner object
            cur2 = textView2.getText().toString().substring(3);                             //Get string from TextView of 2nd spinner
            resTxt = calcCurrency(cur1,cur2,value);
            edTxtCur2=(EditText) findViewById(R.id.edt_cur_2);
            edTxtCur2.setText(resTxt);
        }
    }

    private void btnClearClicked() {    // Υλοποίηση της btnClearClicked
        EditText edTxtCur1,edTxtCur2;

        edTxtCur1=(EditText) findViewById(R.id.edt_cur_1);
        edTxtCur1.setText("");
        edTxtCur2=(EditText) findViewById(R.id.edt_cur_2);
        edTxtCur2.setText("");
    }

    private Double findCurrency(String curToSearch){
        Double result = 0.00001;

        for(String rowMasterData: masterData){
            //Log.i("findCurrency-Calc", curToSearch + " = > " + rowMasterData);
            if (curToSearch.equals(rowMasterData.substring(0,3)))
                return Double.valueOf(rowMasterData.substring(6));
        }
        return result;
    }

    private String calcCurrency(String cur1, String cur2, Double intValue){
        Double conv1,conv2,result;
        Double calculation = 0.00000;

        conv1 = findCurrency(cur1);
        conv2 = findCurrency(cur2);
        result = calculation + (intValue*conv2)/conv1;
        result = Double.valueOf(String.format(Locale.US,"%.2f",result));
        return result.toString();
    }

    private String toastCalcCurrency(int position){
        String cur1,cur2;

        EditText edTxtCur1=(EditText) findViewById(R.id.edt_cur_1);
        if (!edTxtCur1.getText().toString().equals("")) {
            Double value = Double.valueOf(edTxtCur1.getText().toString());
            spnCur1 = (Spinner) findViewById(R.id.spin_cur_1);     //Get 1st spinner object
            TextView textView1 = spnCur1.getSelectedView().findViewById(R.id.spin_txt);     //Get TextView from 1st spinner object
            cur1 = textView1.getText().toString().substring(3);                             //Get string from TextView of 1st spinner
            cur2 = masterData[position].substring(0, 3);                                    //Get 3char currency string from masterData
            if (masterData[position].substring(4, 5).equals("-")) {                         //Check for - in the 4th position
                return ( cur1 + " " + value.toString() + " => " + calcCurrency(cur1, cur2, value) + " " + cur2);
            }
        }
        return "Nothing to calculate.!!!";
    }

    private void showToast(Context context, String text,int duration){

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void newMasterData(String[] newMasterData){  //Insert new data from listMasterData

        masterData = new String[newMasterData.length];         //Clear  data from MasterData
        masterData = Arrays.copyOf(newMasterData, newMasterData.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

       // menu.getItem(0).getIcon();
       // return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.m22_refresh) {
            FetchCurrenciesTask task = new FetchCurrenciesTask();
            task.execute(this);
            return true;
        }
        if (id == R.id.m23_fb_share_button) {

            // Sharing the content to facebook
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                Log.i("FB - Share content", "ShareDialog.canShow(ShareLinkContent.class)=TRUE");
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://github.com/sakists/CurrencyConvertor")).build();
                ShareDialog.show(this,content);  // Show facebook ShareDialog
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
