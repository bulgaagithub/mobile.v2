package com.example.erdenebulgans.soh_appuser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.config.Network;
import com.example.erdenebulgans.soh_appuser.config.datetime;
import com.example.erdenebulgans.soh_appuser.database.SQLite;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //=====Network check====
    Network network;
    //=========//

    //=====Strings====
    private String link;

    private String usercode, password;
    //=========

    //=====JSON====
    private JSONObject jsonObject;
    //=========//

    //=====Boolean====
    boolean session_user;
    //=========//

    //=====Config====
    datetime datetime;
    //=========

    // Cursor
    Cursor cursor;
    //=========

    // Integer
    private  Integer row_id;
    //=========

    // SQLite
    SQLite sqLite;
    //=========

    // EditText
    EditText et_user_code, et_password;
    //=========

    // Button
    Button lgButton, linkButton;
    //=========

    public String accountData;
    // Intent
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         intent = new Intent(MainActivity.this,Main2Activity.class);
        // Button
        lgButton = (Button)findViewById(R.id.lg_btn);
        linkButton = (Button)findViewById(R.id.link_btn);

        // EditText
        et_user_code = (EditText)findViewById(R.id.et_usercode);
        et_password = (EditText)findViewById(R.id.et_password);

        // ClickListener
        lgButton.setOnClickListener(this);
        linkButton.setOnClickListener(this);

        // CheckSession
        CheckSession();
    }

    // onClick function
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lg_btn:
                network = new Network(getApplicationContext());
                try {
                    if (!et_user_code.getText().toString().isEmpty() && !et_password.getText().toString().isEmpty()) {
                        if (network.networkCheck()) {
                            usercode = et_user_code.getText().toString();
                            password = et_password.getText().toString();
                            checkLogin checkLogin = new checkLogin();
                            checkLogin.execute(usercode, getSHA1HashData(password));
                        } else {
                            Toast.makeText(getApplicationContext(), "Интернет холболтоо шалгана уу?", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Хэрэглэгчийн код, нууц үг хоосон байна. Утга оруулна уу!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    e.getMessage();
                }
            break;
            case R.id.link_btn:
                try {
                    Uri webpage = Uri.parse("http://www.voipnice.com/manager/");
                    Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    Toast.makeText(this,"No application can handle this request." + " Please install a web browser",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    // checkLogin return json
    public void logged(String json){

        if (json != null ) {
            try {
                jsonObject = new JSONObject(json);
                int pid = Integer.parseInt(jsonObject.getString("pid"));
                String uid = jsonObject.getString("uID");
                String upassword = jsonObject.getString("upassword");
                String validated = jsonObject.getString("validated");
                String utype = jsonObject.getString("utype");
                datetime = new datetime();
                String logindate = datetime.datetime();
                session_user = Boolean.parseBoolean(validated);
                if (session_user){
                    insertTrueData(pid,uid,upassword,validated,utype,logindate);
                } else {
                    Toast.makeText(getApplicationContext(),"Алдаа гарлаа! хэрэглэгчийн код эсвэл нууц үгээ шалгана уу.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),"Серверт алдаа гарлаа!", Toast.LENGTH_SHORT).show();
            session_user = false;
        }
    }

    // inertTrueData
    public void insertTrueData(Integer pid, String uid, String upassword, String validate, String utype, String login_datetime){

        usercode = et_user_code.getText().toString();
        password = getSHA1HashData(et_password.getText().toString());

        // SQLite database ажиллах
        sqLite = new SQLite(getApplicationContext());
        int row = sqLite.numberOfRows(); // мөрийн тоо авна
        if (row>0) {
        } else {
            // O row байвал insert хийнэ.
            if (sqLite.insertSession(pid,uid,upassword,validate,utype,login_datetime)){
                Log.d("insert","Баазад хууллаа");
            } else {}

            // Cursor
            cursor = sqLite.selectId(usercode,password);
            cursor.moveToNext();
            row_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("pid")));  // хүснэгтэд байгаа мөрийн дугаар
            if (!cursor.isClosed()){
                cursor.close();
            }
            intent.putExtra("session",session_user = true);
            intent.putExtra("row_id",row_id);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Амжилттай нэвтэрлээ.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // checkLogin [:server]
    private class checkLogin extends AsyncTask<String, Void, String>{

        String response = "";
        @Override
        protected void onPreExecute() {
            link = "http://www.voipnice.com/android/login";
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String set_data = "username="+strings[0].toString()+"&"+"password="+strings[1].toString();
                bufferedWriter.write(set_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String row;
                while ((row = bufferedReader.readLine()) != null){
                    response += row;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            logged(s);
        }
    }

    // pass hash function
    private String getSHA1HashData(String data){

        MessageDigest digest = null;
        byte[] input = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            input = digest.digest(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < input.length; i++) {
            int halfbyte = (input[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = input[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    // onBackPressed
    @Override
    public void onBackPressed() {
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.exit = true;
        finish();
    }

    // checkSession function
    public void CheckSession(){
        // Check database row>0 true row<0 false login activity
        // SQLite database ажиллах
        sqLite = new SQLite(this);
        int row = sqLite.numberOfRows(); // мөрийн тоо авна
        if (row>0) {
            cursor = sqLite.sessionSelect();
            cursor.moveToNext();
            String validated = cursor.getString(cursor.getColumnIndex("validated"));
            row_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("pid")));  // хүснэгтэд байгаа мөрийн дугаар
            if (!cursor.isClosed()){
                cursor.close();
            }
            if (validated.equals("true")){
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("session",session_user = true);
                intent.putExtra("row_id",row_id);
                startActivity(intent);
                finish();
                Log.d("logIn","Нэвтэрсэн");
            }else {
                // Toast.makeText(getApplicationContext(),"Нэвтрээгүй",Toast.LENGTH_SHORT).show();
            }
        } else {
            // Toast.makeText(getApplicationContext(),"Нэвтрээгүй",Toast.LENGTH_SHORT).show();
            Log.d("logIn","Нэвтрээгүй");
        }
    }

}
