package gr.tsiriath_android.currencyconvertor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> currenciesListAdapter;
    private Spinner spnCur1,spnCur2;
    private static  String[] masterData = {
            "EUR - 1.0000",
            "AUD - 1.5000",
            "BGN - 2.0000",
            "DKK - 3.0000"};

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
        SpinnerAdapter adapter=new SpinnerAdapter(this,R.layout.spinner_item_currencies,R.id.spin_txt,spnCurList);
        spnCur1.setAdapter(adapter);
        spnCur2.setAdapter(adapter);

        btnSwitch = (Button) findViewById(R.id.btn_switch); //σύνδεση του btnSwitch με το Button Switch
        btnCalc = (Button) findViewById(R.id.btn_calc);     //σύνδεση του btnCalc με το Button Calc
        btnClear = (Button) findViewById(R.id.btn_clear);   //σύνδεση του btnClear με το Button Clear

        btnSwitch.setOnClickListener(switchOnClickListener);//κλήση δημιουργίας listener για το Button Switch
        btnCalc.setOnClickListener(calcOnClickListener);    //κλήση δημιουργίας listener για το Button Calc
        btnClear.setOnClickListener(clearOnClickListener);  //κλήση δημιουργίας listener για το Button Clear

        createMyArrayAdapter(this,R.layout.list_item_currencies,R.id.list_item_currencies_textview,masterData);
        setMyListViewAdapter(R.id.listview_currencies);

        //Set focus on switch button
        //btnSwitch.setFocusableInTouchMode(true);
        spnCur2.requestFocus();
    }

    private ArrayList<ItemData> fillspnCurList() {

        String[][] XMLCurTable;
        Integer curImg;

        ArrayList<ItemData> result =new ArrayList<>();
        XMLCurTable = (new LibCurrenciesXML(this)).getDetailXMLCurTable();   //Get full detail from XMLCurTable
        int arrayLen = XMLCurTable.length;              //Calculate table size
        for(int i=0;i<arrayLen ;i++) {
            curImg = getResources().getIdentifier(XMLCurTable[i][1] , "drawable", getPackageName());   // Convert image name to images's ID
            result.add(new ItemData(" - " + XMLCurTable[i][0], curImg));    // Create a new line for spinner
        }
        return result;
    }

    public void createMyArrayAdapter(Activity myActivity, int myLayoutID, int myItemID, String[] newMasterData){

        currenciesListAdapter = new ArrayAdapter<String>(
                myActivity,
                myLayoutID,
                myItemID,
                Arrays.asList(newMasterData));
    }

    public void setMyListViewAdapter(int myListViewID){
        ListView currenciesListView;

        currenciesListView =  (ListView)findViewById(myListViewID);
        currenciesListView.setAdapter(currenciesListAdapter);
    }

    static void newMasterData(String[] newMasterData){  //Insert new data from listMasterData

        masterData = new String[newMasterData.length];         //Clear  data from MasterData
        masterData = Arrays.copyOf(newMasterData, newMasterData.length);
    }

    private View.OnClickListener switchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnSwitchClicked();  //κλήση της btnClearClicked
        }
    };

    private View.OnClickListener calcOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnCalcClicked();  //κλήση της btnCalcClicked
        }
    };

    private View.OnClickListener clearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnClearClicked();  //κλήση της btnClearClicked
        }
    };


    private void btnSwitchClicked() {    // Υλοποίηση της btnSwitchClicked

        spnCur1=(Spinner)findViewById(R.id.spin_cur_1);
        spnCur2=(Spinner)findViewById(R.id.spin_cur_2);
        Integer oldSp1 =spnCur1.getSelectedItemPosition();          // Save old sp1 item position
        spnCur1.setSelection(spnCur2.getSelectedItemPosition());    // Change sp1 itemPos with sp2 itemPos
        spnCur2.setSelection(oldSp1);                               // Change sp2 with old sp1 itemPos
    }

    private void btnCalcClicked() {    // Υλοποίηση της btnCalcClicked
        Double value,conv1,conv2,result;
        String cur1,cur2;
        String resTxt;
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
            conv1 = findCurrency(cur1);
            conv2 = findCurrency(cur2);
            result = (value*conv2)/conv1;
            result = Double.valueOf(String.format("%.2f",result));  //Reduce results to 2 decimal points
            Log.i("mainAct-Calc",  Double.toString(value) + " , " + cur1 + " , " + cur2 + " , " + Double.toString(conv1) + " , " + Double.toString(conv2));
            resTxt = result.toString();
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

    public Double findCurrency(String curToSearch){
        Double result = 0.001;

        for(String rowMasterData: masterData){
            //Log.i("findCurrency-Calc", curToSearch + " = > " + rowMasterData);
            if (curToSearch.equals(rowMasterData.substring(0,3)))
                return Double.valueOf(rowMasterData.substring(6));
        }
        return result;
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
        if (id == R.id.m23_facebook_post){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
