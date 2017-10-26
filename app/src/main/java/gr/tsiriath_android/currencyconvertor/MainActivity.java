package gr.tsiriath_android.currencyconvertor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity {

    private Button btnClear;
    private EditText edTxtCur1,edTxtCur2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnClear = (Button) findViewById(R.id.btn_clear);   //σύνδεση του btnClear με το Button Clear
        btnClear.setOnClickListener(clearOnClickListener);  //κλήση δημιουργίας listener για το Button Clear

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
        if (id == R.id.m20_options) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
