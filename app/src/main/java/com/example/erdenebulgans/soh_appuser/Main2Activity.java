package com.example.erdenebulgans.soh_appuser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.erdenebulgans.soh_appuser.Items.AccountItems;
import com.example.erdenebulgans.soh_appuser.config.Network;
import com.example.erdenebulgans.soh_appuser.config.datetime;
import com.example.erdenebulgans.soh_appuser.database.SQLite;
import com.example.erdenebulgans.soh_appuser.fragments.Account;
import com.example.erdenebulgans.soh_appuser.fragments.Details;
import com.example.erdenebulgans.soh_appuser.fragments.Fragment1;
import com.example.erdenebulgans.soh_appuser.fragments.Fragment2;
import com.example.erdenebulgans.soh_appuser.fragments.Fragment3;
import com.example.erdenebulgans.soh_appuser.fragments.Hereglee;
import com.example.erdenebulgans.soh_appuser.fragments.Home;
import com.example.erdenebulgans.soh_appuser.fragments.LoadFragment;
import com.example.erdenebulgans.soh_appuser.fragments.Tooluur;
import com.example.erdenebulgans.soh_appuser.fragments.Tulbur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    // Bundle
    Bundle bundle_session;
    // Network
    Network network;
    // Integer
    Integer row_id;
    // SQLite
    SQLite sqLite;
    Cursor cursor;

    // Fragments jsonDatas
    public String paymentData;
    public String usageData;
    public String accountData;
    public String homeData;
    //---------------------------
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView firstname, lastname;
    private boolean check_session;
    //---------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Default autoload
        network = new Network(getApplicationContext());
        sqLite = new SQLite(this);

        // Network true
        if (network.networkCheck()) {
            sqLite = new SQLite(getApplicationContext());
            int row = sqLite.numberOfRows();
            // Нэвтэрсэн бол
            if (row > 0) {
                cursor = sqLite.sessionSelect();
                cursor.moveToNext();
                check_session = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("validated")));
                row_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("pid")));
                drawerLayout = (DrawerLayout)findViewById(R.id.dl);
                toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
                NavigationView nvDrawer = (NavigationView)findViewById(R.id.navigation_view);
                View headerView = nvDrawer.getHeaderView(0);
                firstname = (TextView)headerView.findViewById(R.id.tvName);
                lastname = (TextView)headerView.findViewById(R.id.tv_email);
                firstname.setText("Эрдэнэбулган");
                lastname.setText("Сайнхишиг");
                nvDrawer.setItemIconTintList(null);
                nvDrawer.setItemTextColor(null);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setupDrawerContent(nvDrawer);

                if (!cursor.isClosed()) {
                    cursor.isClosed();
                }
                if(check_session)
                {
                    Fragment fragment = new LoadFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();
                    setTitle("Эхлэл");
                } else {
                    startActivity(new Intent(this,MainActivity.class));
                    destroy();
                    finish();
                }
                home();
                account();
                payment();
                usage();
            } else { // Нэвтрээгүй бол
                Toast.makeText(getApplicationContext(),"Нэвтрээгүй байна!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
        } else { // Network false
            bundle_session = getIntent().getExtras();
            sqLite = new SQLite(getApplicationContext());
            cursor = sqLite.sessionSelect();
            cursor.moveToNext();
            check_session = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("validated")));
            row_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("pid")));
            if (!cursor.isClosed()) {
                cursor.isClosed();
            }
            if(check_session) // validated true
            {
                Fragment fragment = new Home();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,fragment).commit();
                setTitle("Эхлэл");

                // ===============================================
                drawerLayout = (DrawerLayout)findViewById(R.id.dl);
                toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
                NavigationView nvDrawer = (NavigationView)findViewById(R.id.navigation_view);
                View headerView = nvDrawer.getHeaderView(0);
                firstname = (TextView)headerView.findViewById(R.id.tvName);
                lastname = (TextView)headerView.findViewById(R.id.tv_email);
                firstname.setText("Эрдэнэбулган");
                lastname.setText("Сайнхишиг");
                nvDrawer.setItemIconTintList(null);
                nvDrawer.setItemTextColor(null);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setupDrawerContent(nvDrawer);
                Toast.makeText(getApplicationContext(),"Интернет тасарсан байна!",Toast.LENGTH_SHORT).show();
                // =============================================== //
            } else {
                startActivity(new Intent(this,MainActivity.class));
                destroy();
                finish();
            }
        }
    }

    // CloseDrawer function
    public void CloseDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        } else {}
    }

    // selectedItemDrawer
    public void selectedItemDrawer(MenuItem menuItem){
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.home:
                fragmentClass = Home.class;
                fragment(menuItem,fragmentClass);
                break;
            case R.id.tooluur:
                fragmentClass = Tooluur.class;
                fragment(menuItem,fragmentClass);
                break;
            case R.id.hereglee:
                fragmentClass = Hereglee.class;
                fragment(menuItem,fragmentClass);
                break;
            case R.id.tulbur:
                fragmentClass = Tulbur.class;
                fragment(menuItem,fragmentClass);
                break;
            case R.id.account:
                fragmentClass = Account.class;
                fragment(menuItem,fragmentClass);
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Гарах")
                        .setMessage("Системээс гарах уу?")
                        .setIcon(R.drawable.ic_exit_to_app_black_18dp)
                        .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                destroy();
                            }
                        })
                        .setNegativeButton("Үгүй", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                drawerLayout.closeDrawers();
                break;
        }
    }

    // setupDrawerContent
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedItemDrawer(item);
                return true;
            }
        });
    }

    // onBackPressed
    @Override
    public void onBackPressed() {
        if(check_session){
            finish();
        } else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return;
    }

    // onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    // OptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()){
            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Гарах")
                        .setMessage("Системээс гарах уу?")
                        .setIcon(R.drawable.ic_exit_to_app_black_18dp)
                        .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                destroy();
                            }
                        })
                        .setNegativeButton("Үгүй", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
                /*
            case R.id.help:
                Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                return true;
                */
            case R.id.new_password:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                View mView = getLayoutInflater().inflate(R.layout.newpassword,null);
                final EditText oldpassword = (EditText)mView.findViewById(R.id.oldpassword);
                final EditText newpassword = (EditText)mView.findViewById(R.id.newpassword);
                final EditText confpassword = (EditText)mView.findViewById(R.id.confpassword);
                Button submit = (Button)mView.findViewById(R.id.submitBtn);
                Button cancel = (Button)mView.findViewById(R.id.cancelBtn);
                sqLite = new SQLite(getApplicationContext());
                Cursor cursor = sqLite.sessionSelect();
                cursor.moveToNext();

                final String id = cursor.getString(cursor.getColumnIndex("pid"));
                final String username = cursor.getString(cursor.getColumnIndex("uid"));
                final String password = cursor.getString(cursor.getColumnIndex("upassword"));
                final String type = cursor.getString(cursor.getColumnIndex("utype"));
                if (!cursor.isClosed()){
                    cursor.isClosed();
                }

                builder1.setView(mView);
                final AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!oldpassword.getText().toString().isEmpty()) {
                            String oldpass = getSHA1HashData(oldpassword.getText().toString());
                            if (oldpass.equals(password)) {
                                if (!newpassword.getText().toString().isEmpty() && !confpassword.getText().toString().isEmpty()) {
                                    String new_password = getSHA1HashData(newpassword.getText().toString());
                                    String conf = getSHA1HashData(confpassword.getText().toString());
                                    if (new_password.equals(conf)) {
                                        sendPassword send = new sendPassword();
                                        send.execute(oldpassword.getText().toString(), new_password, conf,id,username, password, type);
                                        // Toast.makeText(Main2Activity.this, "Нууц үг амжилттай шинэчлэгдлээ", Toast.LENGTH_SHORT).show();
                                        oldpassword.setText("");
                                        newpassword.setText("");
                                        confpassword.setText("");
                                        alertDialog1.dismiss();
                                    } else {
                                        Toast.makeText(Main2Activity.this, "Хоёр нууц үгээ тааруулна уу.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Main2Activity.this, "Нууц үгээ оруулна уу", Toast.LENGTH_SHORT).show();
                                }
                            } else {Toast.makeText(Main2Activity.this,"Та хуучин нууц үгээ зөв оруулна уу!",Toast.LENGTH_SHORT).show();}
                        } else {
                            Toast.makeText(Main2Activity.this,"Та хуучин нууц үгээ оруулна уу",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // fragment function
    public void fragment(MenuItem menuItem, Class fragmentClass){
        Fragment myFragment = null;
        try {
            myFragment = (Fragment)fragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    // session delete destroy function
    public void destroy(){
        if (check_session){
            sqLite.deleteContact(row_id);
            Log.d("session_delete", "баазаас устгагдлаа.");
            finish();
        }
    }

    // change password sending
    private class sendPassword extends AsyncTask<String, String, String>{

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
       protected String doInBackground(String... strings) {
           try {
               URL url = new URL("http://www.voipnice.com/android/change");
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setDoInput(true);
               httpURLConnection.setDoOutput(true);
               OutputStream outputStream = httpURLConnection.getOutputStream();
               BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
               String send = "oldpassword="+strings[0].toString()+"&"+"newpassword="+strings[1].toString()+"&"+"new2password="+strings[2]
                       +"&"+"id="+strings[3]+"&"+"username="+strings[4]+"&"+"password="+strings[5]+"&"+"type="+strings[6];
               Log.d("sendPost",send);
               bufferedWriter.write(send);
               bufferedWriter.flush();
               bufferedWriter.close();
               outputStream.close();
               InputStream inputStream = httpURLConnection.getInputStream();
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
               String row = "";
               String response;
               while ((response = bufferedReader.readLine()) != null){
                   row += response;
               }

               bufferedReader.close();
               inputStream.close();
               httpURLConnection.disconnect();

               return row;
           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
           return null;
       }

       @Override
       protected void onProgressUpdate(String... values) {
           super.onProgressUpdate(values);
       }

       @Override
       protected void onPostExecute(String result) {
           super.onPostExecute(result);
           changePassword(result);
       }
    }

    // password change check
    public boolean changePassword(String status) {
        int stat = 0;
        if (!status.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(status);
                stat = Integer.parseInt(jsonObject.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (stat == 1) {
                Toast.makeText(getApplicationContext(), "Нууц үг амжилттай солигдлоо", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Амжилтгүй боллоо... Дахин оролдоно уу!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Алдаа гарлаа. Дахин оролдоно уу...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void home(){
        Cursor cursor;
        cursor = sqLite.sessionSelect();
        cursor.moveToNext();
        String username = cursor.getString(cursor.getColumnIndex("uid"));
        String password = cursor.getString(cursor.getColumnIndex("upassword"));
        String id = cursor.getString(cursor.getColumnIndex("pid"));
        String type = cursor.getString(cursor.getColumnIndex("utype"));
        AsyncTask<String,String,String> home = new AsyncTask<String, String, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL("http://www.voipnice.com/android/announcement");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String put = "username=" + strings[0] + "&" + "password=" + strings[1]
                            + "&" + "id=" + strings[2] + "&" + "type=" + strings[3];
                    bufferedWriter.write(put);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String account_data = "";
                    String row;
                    while ((row = bufferedReader.readLine()) != null) {
                        account_data += row;
                    }
                    inputStream.close();
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    return account_data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                homeData = s;
                Log.d("homeData", "" + s);
            }
        };
        home.execute(username,password,id,type);
    }
    public void account(){
        Cursor cursor;
        cursor = sqLite.sessionSelect();
        cursor.moveToNext();
        String username = cursor.getString(cursor.getColumnIndex("uid"));
        String password = cursor.getString(cursor.getColumnIndex("upassword"));
        String id = cursor.getString(cursor.getColumnIndex("pid"));
        String type = cursor.getString(cursor.getColumnIndex("utype"));

        AsyncTask<String,String,String> account = new AsyncTask<String, String, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL("http://www.voipnice.com/android/user_info");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String put = "username=" + strings[0] + "&" + "password=" + strings[1]
                            + "&" + "id=" + strings[2] + "&" + "type=" + strings[3];
                    bufferedWriter.write(put);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String account_data = "";
                    String row;
                    while ((row = bufferedReader.readLine()) != null) {
                        account_data += row;
                    }
                    inputStream.close();
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    return account_data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                accountData = s;
            }
        };
        account.execute(username,password,id,type);
    }
    public void payment(){
        Cursor cursor;
        cursor = sqLite.sessionSelect();
        cursor.moveToNext();
        String username = cursor.getString(cursor.getColumnIndex("uid"));
        String password = cursor.getString(cursor.getColumnIndex("upassword"));
        String id = cursor.getString(cursor.getColumnIndex("pid"));
        String type = cursor.getString(cursor.getColumnIndex("utype"));
        AsyncTask<String,String,String> payment = new AsyncTask<String, String, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL("http://www.voipnice.com/android/payment");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String put = "username=" + strings[0] + "&" + "password=" + strings[1]
                            + "&" + "id=" + strings[2] + "&" + "type=" + strings[3];
                    bufferedWriter.write(put);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String account_data = "";
                    String row;
                    while ((row = bufferedReader.readLine()) != null) {
                        account_data += row;
                    }
                    inputStream.close();
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    return account_data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                paymentData = s;
                Log.d("bulgaa", "" + s);
            }
        };
        payment.execute(username,password,id,type);
    }
    public void usage(){
        Cursor cursor;
        cursor = sqLite.sessionSelect();
        cursor.moveToNext();
        String username = cursor.getString(cursor.getColumnIndex("uid"));
        String password = cursor.getString(cursor.getColumnIndex("upassword"));
        String id = cursor.getString(cursor.getColumnIndex("pid"));
        String type = cursor.getString(cursor.getColumnIndex("utype"));
        AsyncTask<String,String,String> usage = new AsyncTask<String, String, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL("http://www.voipnice.com/android/usage");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String put = "username=" + strings[0] + "&" + "password=" + strings[1]
                            + "&" + "id=" + strings[2] + "&" + "type=" + strings[3];
                    bufferedWriter.write(put);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String account_data = "";
                    String row;
                    while ((row = bufferedReader.readLine()) != null) {
                        account_data += row;
                    }
                    inputStream.close();
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    return account_data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                usageData = s;
                Log.d("bulgaa", "" + s);
            }
        };
        usage.execute(username,password,id,type);
    }
    public String getMyData() {
        if (getTitle().equals("Хувийн мэдээлэл")){
            return accountData;
        } else if (getTitle().equals("Төлбөр")) {
            return paymentData;
        } else if (getTitle().equals("Хэрэглээ")) {
            return usageData;
        } else
        {
            return null;
        }
    }
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
}
