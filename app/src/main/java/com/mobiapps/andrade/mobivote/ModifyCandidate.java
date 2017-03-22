package com.mobiapps.andrade.mobivote;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class ModifyCandidate extends AppCompatActivity {

    private Button saveBtn, deleteBtn;
    private EditText nameEdt, idEdt;
    private Spinner posSpn;
    private ArrayAdapter adapter;
    private String[] pos = {"President", "Vice President", "Secretary"};
    String positions="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_candidate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Modify Candidate");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveBtn = (Button) findViewById(R.id.btnSave);
        deleteBtn = (Button) findViewById(R.id.btnDelete);
        nameEdt = (EditText) findViewById(R.id.edtName);
        idEdt = (EditText) findViewById(R.id.edtCandId);
        posSpn = (Spinner) findViewById(R.id.spnPos);


        Intent intent = getIntent();
        String name = intent.getStringExtra("data");
        String poss = intent.getStringExtra("pos");

        nameEdt.setText(name);

        if (poss.equals("President")) {
            posSpn.setSelection(0);
        } else if (poss.equals("Vice President")) {
            posSpn.setSelection(1);
        } else if (poss.equals("Secretary")) {
            posSpn.setSelection(2);
        }

        final Database db = new Database(this);
        Cursor res = db.getCandiateId(nameEdt.getText().toString());
        while (res.moveToNext()){
            idEdt.setText(res.getString(0));
        }

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
                TextView posi = (TextView) view;
                positions = posi.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isupdate = db.updateCandidate(idEdt.getText().toString(),positions,nameEdt.getText().toString());
                if (isupdate==true){
                    Toast.makeText(getApplicationContext(),"Data updated", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ModifyCandidate.this,Candidates.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Data not Updated",Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(ModifyCandidate.this, R.style.MyDialogTheme);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete "+nameEdt.getText().toString()+" ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deleted = db.deleteCandidate(idEdt.getText().toString());
                        if(deleted > 0){
                            Toast.makeText(ModifyCandidate.this, "Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ModifyCandidate.this,UsersActivity.class));
                        }
                        else {
                            Toast.makeText(ModifyCandidate.this, "Not Deleted", Toast.LENGTH_SHORT).show();
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
            ModifyCandidate.this.finish();
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
