package com.mobiapps.andrade.mobivote;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private ListView usersLv;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Voters List");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton addfab = (FloatingActionButton) findViewById(R.id.fabAdd);
        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UsersActivity.this, AddVoter.class));
            }
        });

        Database db = new Database(this);

        usersLv = (ListView) findViewById(R.id.lvUsers);
        Cursor res = db.getAllUsers();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                textView.setPadding(15,15,15,15);
                return textView;

            }
    };
        while (res.moveToNext()){
            listItems.add(res.getString(0)+"\n"+res.getString(1)+"\n"+res.getString(2)+"\n"+res.getString(3));
            adapter.notifyDataSetChanged();
        }

        usersLv.setAdapter(adapter);
        usersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UsersActivity.this, listItems.get(position), Toast.LENGTH_SHORT).show();

                Intent editUser = new Intent(UsersActivity.this, ModifyUser.class);
                editUser.putExtra("data", listItems.get(position));
                startActivity(editUser);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            UsersActivity.this.finish();
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
