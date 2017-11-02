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
import java.util.ArrayList;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {

    private Button btnSwitch,btnClear;
    private Spinner spnCur1,spbCur2,spnTmp;
    private EditText edTxtCur1,edTxtCur2;

    private ArrayAdapter<String> currenciesListAdapter;
    private ArrayList<ItemData> spnCurList;
    private ListView currenciesListView;
    private String[] sampleData = {
            "AUD - 1.0000",
            "BGN - 1.0000",
            "DKK - 1.0000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spnCurList = fillspnCurList();  //Create parametrical spinner table
        Spinner sp1=(Spinner)findViewById(R.id.spin_cur_1);
        Spinner sp2=(Spinner)findViewById(R.id.spin_cur_2);
        SpinnerAdapter adapter=new SpinnerAdapter(this,R.layout.spinner_item_currencies,R.id.spin_txt,spnCurList);
        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter);

        btnSwitch = (Button) findViewById(R.id.btn_switch);   //σύνδεση του btnClear με το Button Clear
        btnSwitch.setOnClickListener(switchOnClickListener);  //κλήση δημιουργίας listener για το Button Clear
        btnClear = (Button) findViewById(R.id.btn_clear);   //σύνδεση του btnClear με το Button Clear
        btnClear.setOnClickListener(clearOnClickListener);  //κλήση δημιουργίας listener για το Button Clear

        createMyArrayAdapter(this,R.layout.list_item_currencies,R.id.list_item_currencies_textview,sampleData);
        setMyListViewAdapter(R.id.listview_currencies);
        //Set focus on switch button
        btnSwitch.setFocusableInTouchMode(true);
        btnSwitch.requestFocus();
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


    public void createMyArrayAdapter(Activity myActivity, int myLayoutID, int myItemID, String[] mySampleData){

        currenciesListAdapter = new ArrayAdapter<String>(
                myActivity,
                myLayoutID,
                myItemID,
                Arrays.asList(mySampleData));
    }

    public void setMyListViewAdapter(int myListViewID){

        currenciesListView =  (ListView)findViewById(myListViewID);
        currenciesListView.setAdapter(currenciesListAdapter);
    }

    private View.OnClickListener clearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnClearClicked();  //κλήση της btnClearClicked
        }
    };

    private void btnClearClicked() {    // Υλοποίηση της btnClearClicked
        edTxtCur1=(EditText) findViewById(R.id.edt_cur_1);
        edTxtCur1.setText("");
        edTxtCur2=(EditText) findViewById(R.id.edt_cur_2);
        edTxtCur2.setText("");
    }

    private View.OnClickListener switchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnSwitchClicked();  //κλήση της btnClearClicked
        }
    };

    private void btnSwitchClicked() {    // Υλοποίηση της btnSwitchClicked


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
