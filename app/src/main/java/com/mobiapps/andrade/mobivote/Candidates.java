package com.mobiapps.andrade.mobivote;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Candidates extends AppCompatActivity {

    private Spinner posSpn;
    private ListView candLv;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String[] positions = {"President", "Vice President", "Secretary"};
    private ArrayAdapter spnAdapter;
    private String poss="";
    private Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Candidates");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Candidates.this,AddCandidate.class));
            }
        });

        candLv = (ListView) findViewById(R.id.lvCand);
        posSpn = (Spinner) findViewById(R.id.spnPos);
        spnAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,positions){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                textView.setPadding(15,15,15,15);
                return textView;

            }
        };
        posSpn.setAdapter(spnAdapter);

        final Database db = new Database(Candidates.this);

        posSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView pos = (TextView) view;

                if(pos.getText().toString().equals("President")){
                    listItems.clear();
                    res = db.getAllCandiatesPres();
                    poss = "President";
                }
                else if(pos.getText().toString().equals("Vice President")){
                    listItems.clear();
                    poss = "Vice President";
                    res = db.getAllCandiatesVP();
                } else if(pos.getText().toString().equals("Secretary")){
                    listItems.clear();
                    poss = "Secretary";
                    res = db.getAllCandiatesSec();
                }

                adapter = new ArrayAdapter<String>(Candidates.this,android.R.layout.simple_list_item_1,listItems){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        textView.setTextColor(Color.WHITE);
                        textView.setPadding(15,15,15,15);
                        return textView;

                    }
                };
                while (res.moveToNext()){
                    listItems.add(res.getString(2));
                    adapter.notifyDataSetChanged();
                }

                candLv.setAdapter(adapter);
                candLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(Candidates.this, listItems.get(position), Toast.LENGTH_SHORT).show();
                        Intent editCand = new Intent(Candidates.this, ModifyCandidate.class);
                        editCand.putExtra("data", listItems.get(position));
                        editCand.putExtra("pos", poss);
                        startActivity(editCand);

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Candidates.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
