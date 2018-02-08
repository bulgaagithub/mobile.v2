package com.example.erdenebulgans.soh_appuser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Erdenebulgan.S on 1/6/2018.
 */

public class SQLite extends SQLiteOpenHelper{

    private static final String DB_NAME = "auth.db";
    public SQLite(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table user_auth"
        +"(pid integer primary key, uid text, upassword text, validated text, utype text, login_datetime text,loading text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user_auth");
        onCreate(sqLiteDatabase);
    }

    public boolean insertSession(Integer pid, String uid, String upassword, String validated, String utype, String login_datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pid",pid);
        contentValues.put("uid",uid);
        contentValues.put("upassword",upassword);
        contentValues.put("validated",validated);
        contentValues.put("utype",utype);
        contentValues.put("login_datetime",login_datetime);
        contentValues.put("loading","false");
        db.insert("user_auth",null,contentValues);
        return true;
    }

    public Cursor selectId(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pid,uid,validated FROM user_auth WHERE uid = '"+username+"' and upassword = '"+password+"'",null);
        return cursor;
    }

    public Cursor sessionSelect(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_auth",null);
        return cursor;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("user_auth","pid = ? ", new String[]{Integer.toString(id)});
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,"user_auth");
        return numRows;
    }

    public boolean updateSession(Integer pid, String validate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pid",pid);
        contentValues.put("validated",validate);
        db.update("user_auth",contentValues,"pid = ? ",new String[]{Integer.toString(pid)});
        return true;
    }

    public boolean updateLoading(Integer pid, String loading){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pid",pid);
        contentValues.put("loading",loading);
        db.update("user_auth",contentValues,"pid = ? ",new String[]{Integer.toString(pid)});
        return true;
    }
}
