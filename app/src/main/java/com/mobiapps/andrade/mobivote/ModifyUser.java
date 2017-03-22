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

public class ModifyUser extends AppCompatActivity {

    private EditText idEdt, fnameEdt, lNameEdt, statusEdt;
    private Button saveBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Modify Voter");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String[] dataSplit = data.split("\n");
        String data1 = dataSplit[0];
        String data2 = dataSplit[1];
        String data3 = dataSplit[2];
        String data4 = dataSplit[3];

        idEdt = (EditText) findViewById(R.id.edtVotersId);
        fnameEdt = (EditText) findViewById(R.id.edtFname);
        lNameEdt = (EditText) findViewById(R.id.edtLname);
        statusEdt = (EditText) findViewById(R.id.edtStatus);
        saveBtn = (Button) findViewById(R.id.btnSave);
        deleteBtn = (Button) findViewById(R.id.btnDelete);

        idEdt.setText(data1);
        fnameEdt.setText(data2);
        lNameEdt.setText(data3);
        statusEdt.setText(data4);
        final String fullname = ""+data2+" "+data3;

        final Database db = new Database(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isupdate = db.updateVoter(idEdt.getText().toString(), fnameEdt.getText().toString(),lNameEdt.getText().toString(),statusEdt.getText().toString());
                if (isupdate==true){
                    Toast.makeText(getApplicationContext(),"Data updated", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ModifyUser.this,UsersActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Data not Updated",Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(ModifyUser.this, R.style.MyDialogTheme);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete "+fullname+" ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deleted = db.deleteUser(idEdt.getText().toString());
                        if(deleted > 0){
                            Toast.makeText(ModifyUser.this, "Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ModifyUser.this,UsersActivity.class));
                        }
                        else {
                            Toast.makeText(ModifyUser.this, "Not Deleted", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ModifyUser.this.finish();
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
