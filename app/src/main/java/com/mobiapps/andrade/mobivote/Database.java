package com.mobiapps.andrade.mobivote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Onyok on 3/19/2017.
 */

public class Database extends SQLiteOpenHelper {

    public static final String dbName = "MobiVoteDB";
    public static final String usersTblName = "usersTbl";
    public static final String userCol1 = "id";
    public static final String userCol2 = "fName";
    public static final String userCol3 = "lName";
    public static final String userCol4 = "status";
    public static final String candTblName = "candidatesTbl";
    public static final String cCol1 = "pos";
    public static final String cCol2 = "name";
    public static final String cCol3 = "vote";
    public static final String voteTblName = "votesTbl";
    public static final String vCol1 = "id";
    public static final String vCol2 = "pos";
    public static final String vCol3 = "cand";

    public Database(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+usersTblName+" (id TEXT, fName TEXT, lName TEXT, status TEXT)");
        db.execSQL("create table "+candTblName+" (id INTEGER PRIMARY KEY AUTOINCREMENT, pos TEXT, name TEXT, vote INTEGER)");
        db.execSQL("create table "+voteTblName+" (id TEXT, pos TEXT, cand TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+usersTblName);
        db.execSQL("DROP TABLE IF EXISTS "+candTblName);
        db.execSQL("DROP TABLE IF EXISTS "+voteTblName);
        onCreate(db);
    }

    public boolean insertVote(String id, String pos, String cand){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(vCol1, id);
        cont.put(vCol2, pos);
        cont.put(vCol3, cand);
        long res = db.insert(voteTblName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public boolean insertUser(String id, String fName, String lName, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(userCol1, id);
        cont.put(userCol2, fName);
        cont.put(userCol3, lName);
        cont.put(userCol4, status);
        long res = db.insert(usersTblName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public Cursor getUserVotes(String id, String pos){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+voteTblName+" WHERE id='"+id+"' AND pos='"+pos+"'", null);
        return res;
    }

    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+usersTblName, null);
        return res;
    }

    public Cursor getAllUsersId(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+usersTblName+" WHERE id='"+id+"'", null);
        return res;
    }

    public Cursor getAllUsersNotVoted(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+usersTblName+" WHERE id='"+id+"' AND status='Not Voted'", null);
        return res;
    }

    public Integer deleteUser(String cont){

        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(userCol1, cont);
        return dbName.delete(usersTblName, "id=?", new String[]{cont});

    }

    public boolean updateVoter (String id, String fname, String lname, String status){
        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(userCol2,fname);
        contentValues.put(userCol3,lname);
        contentValues.put(userCol4,status);
        dbName.update(usersTblName,contentValues, "id = ?", new String[] {id});
        return true;
    }

    public boolean updateVoterVoted (String id, String status){
        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(userCol4,status);
        dbName.update(usersTblName,contentValues, "id = ?", new String[] {id});
        return true;
    }

    public boolean insertCandidate(String pos, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(cCol1, pos);
        cont.put(cCol2, name);
        long res = db.insert(candTblName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public Cursor getCandiateId(String idid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+candTblName+" WHERE name='"+idid+"'", null);
        return res;
    }


    public Cursor getAllCandiatesPres(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+candTblName+" WHERE pos='President'", null);
        return res;
    }

    public Cursor getAllCandiatesVP(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+candTblName+" WHERE pos='Vice President'", null);
        return res;
    }

    public Cursor getAllCandiatesSec(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+candTblName+" WHERE pos='Secretary'", null);
        return res;
    }

    public Integer deleteCandidate(String cont){

        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cCol2, cont);
        return dbName.delete(usersTblName, "id=?", new String[]{cont});

    }

    public boolean updateCandidate (String id, String pos, String name){
        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cCol1,pos);
        contentValues.put(cCol2,name);
        dbName.update(candTblName,contentValues, "id = ?", new String[] {id});
        return true;
    }

    public boolean updateCandidateVotes (String name, int numVote){
        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cCol3,numVote);
        dbName.update(candTblName,contentValues, "name = ?", new String[] {name});
        return true;
    }

}
