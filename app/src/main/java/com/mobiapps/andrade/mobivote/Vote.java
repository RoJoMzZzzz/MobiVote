package com.mobiapps.andrade.mobivote;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Vote extends AppCompatActivity {

    private Spinner posSpn;
    private String[] pos = {"President", "Vice President", "Secretary"};
    private ArrayAdapter adapter;
    private Cursor res;
    private int numCand;
    private Button castVoteBtn;
    private LinearLayout ll;
    private Database db;
    private String idnguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Vote");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        idnguser = intent.getStringExtra("userID");

        castVoteBtn = (Button) findViewById(R.id.btnCastVote);
        db = new Database(this);
        ll = (LinearLayout) findViewById(R.id.ll);
        posSpn = (Spinner) findViewById(R.id.spnPos);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
                final TextView pos = (TextView) view;
                StringBuffer buffer = new StringBuffer();

                if(pos.getText().toString().equals("President")){
                    ll.removeAllViews();
                    res = db.getAllCandiatesPres();
                    //Voting();
                } else if(pos.getText().toString().equals("Vice President")){
                    ll.removeAllViews();
                    res = db.getAllCandiatesVP();
                    //Voting();
                } else if(pos.getText().toString().equals("Secretary")){
                    ll.removeAllViews();
                    res = db.getAllCandiatesSec();
                    //Voting();
                }

                numCand = res.getCount();
                final RadioButton[] rb = new RadioButton[numCand];
                RadioGroup rg = new RadioGroup(Vote.this);
                rg.setOrientation(RadioGroup.VERTICAL);
                int i = 0;
                while (res.moveToNext()){
                    rb[i]  = new RadioButton(Vote.this);
                    rb[i].setText(res.getString(2));
                    rb[i].setId(i);
                    rb[i].setTextColor(Color.WHITE);
                    int kulay = Color.WHITE;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rb[i].setButtonTintList(ColorStateList.valueOf(kulay));
                    }
                    rg.addView(rb[i]);
                    i++;
                }
                ll.addView(rg);

                final String[] voted = new String[1];
                final int[] prevVote = new int[1];
                castVoteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cursor resChk = db.getUserVotes(idnguser,pos.getText().toString());
                        if (resChk.getCount()>0){
                            showMessage("Error","You already voted a "+pos.getText().toString());
                        } else {

                            for (int a=0; a<numCand; a++){
                                if (rb[a].isChecked()){
                                    voted[0] =rb[a].getText().toString();
                                }
                            }

                            if(voted[0] == null){
                                showMessage("Error", "You did not select a candidate");
                            } else {

                                AlertDialog.Builder builder=new AlertDialog.Builder(Vote.this, R.style.MyDialogTheme);
                                builder.setCancelable(true);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Do you want to vote "+voted[0]+" for "+pos.getText().toString()+" ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Cursor res = db.getCandiateId(voted[0]);
                                        while (res.moveToNext()) {
                                            prevVote[0] = res.getInt(3);
                                        }

                                        prevVote[0]++;
                                        boolean isUpdated = db.updateCandidateVotes(voted[0], prevVote[0]);
                                        if (isUpdated) {
                                            //Toast.makeText(Vote.this, "Data Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Toast.makeText(Vote.this, "Data Not Updated", Toast.LENGTH_SHORT).show();
                                        }

                                        boolean inserted = db.insertVote(idnguser, pos.getText().toString(), voted[0]);
                                        if (inserted) {
                                            //Toast.makeText(Vote.this, "Inserted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Toast.makeText(Vote.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                                        }
                                        //castVoteBtn.setEnabled(false);
                                        boolean upVoter = db.updateVoterVoted(idnguser, "Voted");
                                        if (upVoter) {
                                            //Toast.makeText(Vote.this, "Status Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Toast.makeText(Vote.this, "Status Not Updated", Toast.LENGTH_SHORT).show();
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
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            Vote.this.finish();
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
