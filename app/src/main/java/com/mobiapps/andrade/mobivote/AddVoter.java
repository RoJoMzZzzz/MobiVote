package com.mobiapps.andrade.mobivote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddVoter extends AppCompatActivity {

    private EditText idEdt, fnameEdt, lnameEdt;
    private Button addVoterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Voter");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idEdt = (EditText) findViewById(R.id.edtVotersId);
        fnameEdt = (EditText) findViewById(R.id.edtFname);
        lnameEdt = (EditText) findViewById(R.id.edtLname);
        final String fullname = ""+fnameEdt.getText().toString()+" "+lnameEdt.getText().toString();
        addVoterBtn = (Button) findViewById(R.id.btnAddVoter);
        addVoterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Database db = new Database(AddVoter.this);
                if(idEdt.getText().toString().trim().isEmpty()){
                    idEdt.setError("This item can't be empty");
                }
                else if(fnameEdt.getText().toString().trim().isEmpty()){
                    fnameEdt.setError("This item can't be empty");
                } else if(lnameEdt.getText().toString().trim().isEmpty()){
                    lnameEdt.setError("This item can't be empty");
                }

                else {

                    AlertDialog.Builder builder=new AlertDialog.Builder(AddVoter.this, R.style.MyDialogTheme);
                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to add "+fullname+" to voters ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            boolean insCont = db.insertUser(idEdt.getText().toString(), fnameEdt.getText().toString(), lnameEdt.getText().toString(),"Not Voted");
                            if (insCont){
                                //Toast.makeText(AddVoter.this, "inserted", Toast.LENGTH_LONG).show();
                                //showMessage("Success",""+fnameEdt.getText().toString()+" "+lnameEdt.getText().toString()+" successfully added to voters list");
                                startActivity(new Intent(AddVoter.this, UsersActivity.class));
                            }
                            else{
                                //Toast.makeText(AddVoter.this, "not inserted", Toast.LENGTH_LONG).show();
                                showMessage("Error","Voter not inserted");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddVoter.this.finish();
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
