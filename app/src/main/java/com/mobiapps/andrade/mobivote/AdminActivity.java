package com.mobiapps.andrade.mobivote;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {

    private Button usersBtn, candidatesBtn;
    private String[] pos = {"President", "Vice President", "Secretary"};
    private ArrayAdapter adapter;
    private Spinner posSpn;
    private TextView resTxt,numTxt;
    private String poss="";
    private Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Admin");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usersBtn = (Button) findViewById(R.id.btnUsers);
        candidatesBtn = (Button) findViewById(R.id.btnCandidates);
        posSpn = (Spinner) findViewById(R.id.spnPos);
        resTxt = (TextView) findViewById(R.id.txtResults);
        numTxt = (TextView) findViewById(R.id.txtNumVotes);

        usersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,UsersActivity.class));
            }
        });

        candidatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,Candidates.class));
            }
        });

        final Database db = new Database(this);

        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, pos){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                textView.setPadding(15,15,15,15);
                return textView;

            }
        };
        posSpn.setAdapter(adapter);
        posSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView pos = (TextView) view;
                poss = pos.getText().toString();
                StringBuffer buffer = new StringBuffer();
                StringBuffer buffer1 = new StringBuffer();
                if(pos.getText().toString().equals("President")){
                    res = db.getAllCandiatesPres();
                }
                else if(pos.getText().toString().equals("Vice President")){
                    res = db.getAllCandiatesVP();
                } else if(pos.getText().toString().equals("Secretary")){
                    res = db.getAllCandiatesSec();
                }

                while (res.moveToNext()){
                    buffer.append(res.getString(2)+"\n\n");
                    buffer1.append(""+res.getInt(3)+"\n\n");
                }
                resTxt.setText(buffer.toString());
                numTxt.setText(buffer1.toString());
                //buffer.delete(0, buffer.length());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AdminActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
