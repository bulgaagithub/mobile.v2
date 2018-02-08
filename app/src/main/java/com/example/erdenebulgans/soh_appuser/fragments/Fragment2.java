package com.example.erdenebulgans.soh_appuser.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {

    ArrayList<Data> datasList;
    private ListAdapter listAdapter;
    ListView listView2;
    private Network network;
    private String warmData;
    private String defaultWarmData;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        Main2Activity activity = (Main2Activity)getActivity();
        warmData = activity.usageData;
        datasList = new ArrayList<>();
        defaultWarmData = "{\"warm\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"cold\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"electric\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"electric_night\":[{\"date\":0,\"count\":0,\"usage\":0}]}";
        if (network.networkCheck()) {
            try {
                JSONObject parentObject = new JSONObject(warmData);
                JSONArray jsonArray = parentObject.getJSONArray("warm");
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Data data = new Data(jsonObject.getString("date"), jsonObject.getString("count"), jsonObject.getString("usage"));
                    datasList.add(data);
                }
            } catch (JSONException e) {
                    e.printStackTrace();
            }
        } else {
            try {
                JSONObject parentObject = new JSONObject(defaultWarmData);
                JSONArray jsonArray = parentObject.getJSONArray("warm");
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Data data = new Data(jsonObject.getString("date"),jsonObject.getString("count"),jsonObject.getString("usage"));
                    datasList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        listView2 = (ListView) view.findViewById(R.id.listview2);
        if (network.networkCheck()) {
            listAdapter = new ListAdapter(getActivity().getApplicationContext(), datasList);
            listView2.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        } else {
            listAdapter = new ListAdapter(getActivity().getApplicationContext(), datasList);
            listView2.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }

        return view;
    }

    // List Adapter
    public class ListAdapter extends BaseAdapter {

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
            TextView count = (TextView)view.findViewById(R.id.zaalt);
            TextView usage = (TextView)view.findViewById(R.id.use);

            date.setText(datas.get(i).getDateList());
            count.setText(datas.get(i).getCountList());
            usage.setText(datas.get(i).getUsageList());
            return view;
        }
    }

    public class Data{

        private String dateList;
        private String countList;
        private String usageList;

        public Data(String dateList, String countList, String usageList) {
            this.dateList = dateList;
            this.countList = countList;
            this.usageList = usageList;
        }

        public String getDateList() {
            return dateList;
        }

        public void setDateList(String dateList) {
            this.dateList = dateList;
        }

        public String getCountList() {
            return countList;
        }

        public void setCountList(String countList) {
            this.countList = countList;
        }

        public String getUsageList() {
            return usageList;
        }

        public void setUsageList(String usageList) {
            this.usageList = usageList;
        }
    }

}
