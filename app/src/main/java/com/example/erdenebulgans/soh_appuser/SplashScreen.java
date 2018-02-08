package com.example.erdenebulgans.soh_appuser;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.database.SQLite;

/**
 * Created by Erdenebulgan.S on 1/6/2018.
 */

public class SplashScreen extends AppCompatActivity{

    //=====Handler====
    Handler handler;
    //=========

    // exit validate
    boolean exit = false;
    //=========

    //=====SQLite database====
    SQLite sqLite;
    //=========

    //=====Cursor====
    Cursor cursor;
    //=========

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        //===== activity finish value====
        //=====sessionCheck====
        if (sessionCheck()){
        } else {
            // Toast.makeText(getApplicationContext(),"Нэвтрээгүй",Toast.LENGTH_SHORT).show();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1500);
        }
        if (exit){
            SplashScreen.super.finish();
        }
    }

    // sessionCheck function
    public boolean sessionCheck(){

        // Check database row>0 true row<0 false login activity
        // SQLite database ажиллах
        sqLite = new SQLite(this);
        int row = sqLite.numberOfRows(); // мөрийн тоо авна
        if (row>0) {
            cursor = sqLite.sessionSelect();
            cursor.moveToNext();
            String validated = cursor.getString(cursor.getColumnIndex("validated"));
            if (!cursor.isClosed()){
                cursor.close();
            }
            if (validated.equals("true")){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                // Toast.makeText(getApplicationContext(),"Нэвтрээгүй",Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }
}
