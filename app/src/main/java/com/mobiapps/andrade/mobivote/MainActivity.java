package com.mobiapps.andrade.mobivote;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.TextInputLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userIdEdt;
    private TextInputLayout userIdInputLayout;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Database db = new Database(this);

        userIdEdt = (EditText) findViewById(R.id.edtUserId);
        userIdInputLayout = (TextInputLayout) findViewById(R.id.input_userId);
        startBtn = (Button) findViewById(R.id.btnStart);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor chkId = db.getAllUsersId(userIdEdt.getText().toString());
                Cursor chkIdNotVoted = db.getAllUsersNotVoted(userIdEdt.getText().toString());
                if (userIdEdt.getText().toString().trim().isEmpty()) {
                    userIdEdt.setError("Please Input User ID");
                } else if(userIdEdt.getText().toString().equalsIgnoreCase("K1120263")){
                    startActivity(new Intent(MainActivity.this,AdminActivity.class));
                } else if(chkId.getCount() == 1){
                    if (chkIdNotVoted.getCount() == 1) {
                        Intent intent = new Intent(MainActivity.this, Vote.class);
                        intent.putExtra("userID", userIdEdt.getText().toString());
                        startActivity(intent);
                    } else {
                        showMessage("Error","You already voted");
                        // Toast.makeText(MainActivity.this,"You Already Voted",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showMessage("Error","User not registered");
                    //Toast.makeText(MainActivity.this, "Unknown USER", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
