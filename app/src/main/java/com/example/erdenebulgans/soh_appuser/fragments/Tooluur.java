package com.example.erdenebulgans.soh_appuser.fragments;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.Main2Activity;
import com.example.erdenebulgans.soh_appuser.R;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tooluur extends Fragment {

    datetime datetime;
    private EditText warm, cold, electric;
    private Button button;
    private SQLite sqLite;
    private Cursor cursor;
    private Network network;
    String ognoo;

    // Required empty public constructor
    public Tooluur() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        if (network.networkCheck()) {
        } else {
            Toast.makeText(getActivity().getApplicationContext(),"Интернет тасарсан байна!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tooluur, container, false);
        datetime = new datetime();
        ognoo = datetime.datetime();
        // TextView
        TextView tognoo = (TextView) view.findViewById(R.id.ognoo);
        // EditText
        warm = (EditText) view.findViewById(R.id.haluun_us);
        cold = (EditText) view.findViewById(R.id.huiten_us);
        electric = (EditText) view.findViewById(R.id.tsahilgaan);
        button = (Button) view.findViewById(R.id.sendZaalt);

        // ============================
        if(network.networkCheck()) {
            // button click listener
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!warm.getText().toString().isEmpty()
                            && !cold.getText().toString().isEmpty()
                            && !electric.getText().toString().isEmpty()) {
                        new sendCounter().execute(cold.getText().toString(), warm.getText().toString(), electric.getText().toString());
                        warm.setText("");
                        cold.setText("");
                        electric.setText("");
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Заалт оруулна уу!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // setText
            tognoo.setText(ognoo);
        } else {
            button.setVisibility(View.GONE);
            Toast.makeText(getActivity().getApplicationContext(),"Интернет тасарсан байна!",Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    // sending Counter data
    private class sendCounter extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                // User json data SEND
                sqLite = new SQLite(getActivity().getApplicationContext());
                cursor = sqLite.sessionSelect();
                cursor.moveToNext();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pid",cursor.getString(cursor.getColumnIndex("pid")));
                jsonObject.put("utype",cursor.getString(cursor.getColumnIndex("utype")));
                jsonObject.put("date",ognoo);
                String pid = cursor.getString(cursor.getColumnIndex("pid"));
                String utype = cursor.getString(cursor.getColumnIndex("utype"));
                String username = cursor.getString(cursor.getColumnIndex("uid"));
                String password = cursor.getString(cursor.getColumnIndex("upassword"));
                Log.i("pid",""+jsonObject.toString());
                // Close Cursor
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                //==================================================================================

                // URL -> http://www.voipnice.com/android/counter_data
                URL url = new URL("http://www.voipnice.com/android/counter_data");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sendData = "cold="+strings[0]+"&"+"warm="+strings[1]+"&"+"electric="+strings[2]+"&"+"username="+username
                +"&"+"password="+password+"&"+"id="+pid+"&"+"type="+utype
                        +"&"+"date="+ognoo;
                bufferedWriter.write(sendData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String row;
                String response = "";
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
            } catch (JSONException e) {
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
            checkCounter(s);
        }
    }

    // check Counter status 1 or 0
    private void checkCounter(String data) {

        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Заалт амжилттай хадгалагдлаа.",Toast.LENGTH_SHORT).show();
                    usage();
                } else if (status.equals("0")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Заалт оруулахад алдаа гарсан байна. Дахин оруулна уу!",Toast.LENGTH_SHORT).show();
                } else {}
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(),"Сервертэй холбогдсонгүй!...",Toast.LENGTH_SHORT).show();
        }
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
                Main2Activity activity = (Main2Activity)getActivity();
                activity.usageData = s;
            }
        };
        usage.execute(username,password,id,type);
    }
}
