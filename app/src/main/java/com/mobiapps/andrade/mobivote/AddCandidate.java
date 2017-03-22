package com.mobiapps.andrade.mobivote;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddCandidate extends AppCompatActivity {

    private Spinner posSpn;
    private EditText candEdt;
    private Button addCandBtn;
    private ArrayAdapter adapter;
    private String[] pos = {"President", "Vice President", "Secretary"};
    String positions="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Candidate");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posSpn = (Spinner) findViewById(R.id.spnPos);
        candEdt = (EditText) findViewById(R.id.edtCandName);
        addCandBtn = (Button) findViewById(R.id.btnAddCand);

        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, pos)
        {
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
                TextView posi = (TextView) view;
                positions = posi.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addCandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(candEdt.getText().toString().trim().isEmpty()){
                    candEdt.setError("This field can't be empty");
                } else {

                    AlertDialog.Builder builder=new AlertDialog.Builder(AddCandidate.this, R.style.MyDialogTheme);
                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to add "+candEdt.getText().toString()+" to candidates?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Database db = new Database(AddCandidate.this);
                            boolean insCont = db.insertCandidate(positions,candEdt.getText().toString());
                            if (insCont){
                                //Toast.makeText(AddCandidate.this, "inserted", Toast.LENGTH_LONG).show();
                                //showMessage("Success",""+candEdt.getText().toString()+" added to candidates");
                                startActivity(new Intent(AddCandidate.this, Candidates.class));
                            }
                            else{
                                //Toast.makeText(AddCandidate.this, "not inserted", Toast.LENGTH_LONG).show();
                                showMessage("Error","Candidate not added");
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }
            }
        });


    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddCandidate.this.finish();
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

}
