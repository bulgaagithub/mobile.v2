package com.example.erdenebulgans.soh_appuser.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.RecyclerItem;
import com.example.erdenebulgans.soh_appuser.config.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsPayment extends Fragment {

    private ListView listView;
    private Adapter adapter;
    private FloatingActionButton payButton;
    private ArrayList<AmountItems> listItems;
    private Network network;
    public String id;
    private TextView date, time, sum;
    private Bundle bundle;

    public DetailsPayment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItems = new ArrayList<>();
        network = new Network(getActivity().getApplicationContext());
        bundle = new Bundle();
        bundle = getArguments();
        if (network.networkCheck()) {
            String json = "{" + bundle.getString("payment") + "}";
            try {
                JSONObject parentObject = new JSONObject(json);
                JSONArray jsonArray = parentObject.getJSONArray("payment");
                JSONObject jsonObject;
                for (int i = 1; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    AmountItems amountItems = new AmountItems(i,jsonObject.getString("name"), jsonObject.getString("amount"));
                    listItems.add(amountItems);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            String json = "{\"payment\":[{\"id\":1,\"name\":\"интернет байхгүй\",\"amount\":\"интернет байхгүй\",\"date\":\"интернет байхгүй\"},{\"sum\":\"интернет байхгүй\",\"invoice_id\":\"invoice_id\"}]}";
            try {
                JSONObject parentObject = new JSONObject(json);
                JSONArray jsonArray = parentObject.getJSONArray("payment");
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    AmountItems amountItems = new AmountItems(i,jsonObject.getString("name"), jsonObject.getString("amount"));
                    listItems.add(amountItems);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_payment, container, false);

        date = (TextView)view.findViewById(R.id.paymentDate);
        time = (TextView)view.findViewById(R.id.paymentTime);

        date.setText("Огноо : " + bundle.getString("paymentDATE"));
        time.setText("Цаг : " + bundle.getString("paymentTIME"));

        listView = (ListView) view.findViewById(R.id.paymentList);
        adapter = new Adapter(listItems,getActivity().getApplicationContext());
        listView.setAdapter(adapter);

        sum = (TextView)view.findViewById(R.id.sumPayment);

        sum.setText("Нийт : " + bundle.getString("sum") + "₮");

        payButton = (FloatingActionButton)view.findViewById(R.id.paymentBack);

        // ===========================
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tulbur payment = new Tulbur();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,payment).commit();
            }
        });
        // =============================
        return view;
    }

    // Adapter
    private class Adapter extends BaseAdapter{

        private ArrayList<AmountItems> items;
        private Context mContext;

        public Adapter(ArrayList<AmountItems> items, Context mContext) {
            this.items = items;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return listItems.size();
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
            view = getLayoutInflater().inflate(R.layout.payment,null);
            TextView payName;
            TextView payAmount;
            TextView txtindex;

            payName = (TextView)view.findViewById(R.id.payName);
            payAmount = (TextView)view.findViewById(R.id.payAmount);
            txtindex = (TextView)view.findViewById(R.id.txtindex);

            payName.setText(items.get(i).getNameList());
            payAmount.setText(items.get(i).getAmountList()+"₮");
            txtindex.setText(String.valueOf(items.get(i).getIndexList()));
            return view;
        }
    }

    // amount
    private class AmountItems {
        private int indexList;
        private String nameList;
        private String amountList;

        public AmountItems(int indexList, String nameList, String amountList) {
            this.indexList = indexList;
            this.nameList = nameList;
            this.amountList = amountList;
        }

        public int getIndexList() {
            return indexList;
        }

        public void setIndexList(int indexList) {
            this.indexList = indexList;
        }

        public String getNameList() {
            return nameList;
        }

        public void setNameList(String nameList) {
            this.nameList = nameList;
        }

        public String getAmountList() {
            return amountList;
        }

        public void setAmountList(String amountList) {
            this.amountList = amountList;
        }
    }

}
