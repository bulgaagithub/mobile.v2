package com.example.erdenebulgans.soh_appuser.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import org.w3c.dom.Text;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment {

    private ArrayList<Data> dataList;
    private ListView listView3;
    private ListAdapter adapter2;
    private String electric;
    private Network network;
    private String defaultElectric;

    // Required empty public constructor
    public Fragment3() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        Main2Activity activity = (Main2Activity)getActivity();
        electric = activity.usageData;
        dataList = new ArrayList<>();
        defaultElectric = "{\"warm\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"cold\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"electric\":[{\"date\":\"интернет байхгүй\",\"count\":\"интернет байхгүй\",\"usage\":\"интернет байхгүй\"}],\"electric_night\":[{\"date\":0,\"count\":0,\"usage\":0}]}";
        if (network.networkCheck()){
            try {
                JSONObject parentObject = new JSONObject(electric);
                JSONArray jsonArray = parentObject.getJSONArray("electric");
                JSONObject jsonObject;
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
                JSONObject parentObject = new JSONObject(defaultElectric);
                JSONArray jsonArray = parentObject.getJSONArray("electric");
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Data data = new Data(jsonObject.getString("date"),jsonObject.getString("count"),jsonObject.getString("usage"));
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
        View rootView = inflater.inflate(R.layout.fragment_fragment3, container, false);
        listView3 = (ListView) rootView.findViewById(R.id.listview3);
        if (network.networkCheck()) {
            adapter2 = new ListAdapter(getActivity().getApplicationContext(), dataList);
            listView3.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        } else {
            adapter2 = new ListAdapter(getActivity().getApplicationContext(), dataList);
            listView3.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        }
        return rootView;
    }

    // List Adapter
    private class ListAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<Data> dataList;

        public ListAdapter(Context context, ArrayList<Data> dataList) {
            this.context = context;
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
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

            TextView dates = (TextView)view.findViewById(R.id.date);
            TextView zaalt = (TextView)view.findViewById(R.id.zaalt);
            TextView hereglee = (TextView)view.findViewById(R.id.use);

            dates.setText(dataList.get(i).getDateList());
            zaalt.setText(dataList.get(i).getCountList());
            hereglee.setText(dataList.get(i).getUsageList());

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
