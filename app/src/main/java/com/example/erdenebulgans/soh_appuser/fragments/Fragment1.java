package com.example.erdenebulgans.soh_appuser.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.Main2Activity;
import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.config.Network;
import com.example.erdenebulgans.soh_appuser.database.SQLite;

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
import java.io.PipedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.crypto.interfaces.PBEKey;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    // Lists
    ArrayList<Data> dataList;
    private ListAdapter adapter;
    private Network network;
    private String defaultColdData;
    private ProgressDialog progressDialog;
    // ListView
    ListView listView;
    // String
    private String myData;
    // Required empty public constructor
    public Fragment1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        Main2Activity activity = (Main2Activity) getActivity();
        myData = activity.usageData;
        dataList = new ArrayList<>();
        defaultColdData = "{\"warm\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"cold\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"electric\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"electric_night\":[{\"date\":0,\"count\":0,\"usage\":0}]}";
        if (network.networkCheck()) {
            try {
                JSONObject parentObject = new JSONObject(myData);
                final JSONArray jsonArray = parentObject.getJSONArray("cold");
                JSONObject jsonObject;
                AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Өгөгдлийн уншиж байна...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        }

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                for (int i = 0; i < jsonArray.length(); i++){
                                    Thread.sleep(i);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            progressDialog.dismiss();
                        }
                    };
                task.execute();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Data data = new Data(jsonObject.getString("date"), jsonObject.getString("count"), jsonObject.getString("usage"));
                    dataList.add(data);
                }
            } catch (JSONException e) {
                    e.printStackTrace();
            }

        } else {
            try {
                JSONObject parentObject = new JSONObject(defaultColdData);
                JSONArray jsonArray = parentObject.getJSONArray("cold");
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Data data = new Data(jsonObject.getString("date"), jsonObject.getString("count"), jsonObject.getString("usage"));
                    dataList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        if (network.networkCheck()) {
            listView = (ListView) view.findViewById(R.id.listview);
            adapter = new ListAdapter(getActivity().getApplicationContext(), dataList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            listView = (ListView) view.findViewById(R.id.listview);
            adapter = new ListAdapter(getActivity().getApplicationContext(), dataList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    // List Adapter
    private class ListAdapter extends BaseAdapter{

        private Context context;
        private ArrayList<Data> datas;

        public ListAdapter(Context context, ArrayList<Data> datas) {
            this.context = context;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.list_item,null);
            TextView date = (TextView)view.findViewById(R.id.date);
            TextView zaalt = (TextView)view.findViewById(R.id.zaalt);
            TextView hereglee = (TextView)view.findViewById(R.id.use);

            date.setText(datas.get(i).getDateList());
            zaalt.setText(datas.get(i).getIndexList());
            hereglee.setText(datas.get(i).getUsesList());
            return view;
        }
    }

    // Data Format
    public class Data{

        private String dateList;
        private String indexList;
        private String usesList;

        public Data(String dateList, String indexList, String usesList) {
            this.dateList = dateList;
            this.indexList = indexList;
            this.usesList = usesList;
        }

        public String getDateList() {
            return dateList;
        }

        public void setDateList(String dateList) {
            this.dateList = dateList;
        }

        public String getIndexList() {
            return indexList;
        }

        public void setIndexList(String indexList) {
            this.indexList = indexList;
        }

        public String getUsesList() {
            return usesList;
        }

        public void setUsesList(String userList) {
            this.usesList = userList;
        }
    }


}
