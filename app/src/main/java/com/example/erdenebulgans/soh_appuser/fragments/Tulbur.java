package com.example.erdenebulgans.soh_appuser.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.Main2Activity;
import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.RecyclerItem;
import com.example.erdenebulgans.soh_appuser.config.Network;
import com.example.erdenebulgans.soh_appuser.config.datetime;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tulbur extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;
    private CardAdapter adapter;
    private ArrayList<mainItem> mainItems;
    private String paymentData;
    private Network network;
    private ArrayList<String> paymentList;
    private datetime datetime;

    // Required empty public constructor
    public Tulbur() {}

    @Override // onCreate
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        paymentList = new ArrayList<>();
        if (network.networkCheck()) {
            Main2Activity activity = (Main2Activity) getActivity();
            paymentData = activity.paymentData;
            // listItems
            mainItems = new ArrayList<>();
            if (paymentData != null) {
                try {
                    String items = "";
                    JSONObject parentObject = new JSONObject(paymentData);
                    JSONArray jsonArray;
                    JSONObject object;
                    JSONObject object2;
                    int s = 1;
                    int index;
                    for (int i = 0; i < 1; i++) {
                        do {
                            paymentList.add("\"payment\":" + parentObject.getString("payment" + s));
                            s++;
                        } while (s <= parentObject.length());

                        for (int x = 1; x <= paymentList.size(); x++) {
                            jsonArray = parentObject.getJSONArray("payment" + x);
                            int length = 0;
                            int size = Integer.parseInt("" + jsonArray.length());
                            int size2 = Integer.parseInt("" + jsonArray.length());
                            size = size - 1;
                            size2 = size2 - 2;
                            object = jsonArray.getJSONObject(length);
                            object2 = jsonArray.getJSONObject(size2);
                            mainItem item = new mainItem(object.getString("sum"), object.getString("invoice_id"), object2.getString("date"));
                            mainItems.add(item);
                        }
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            } else {
                Home home = new Home();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,home).commit();
            }
        } else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tulbur, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        textView = (TextView)view.findViewById(R.id.networkCheck);
        if (network.networkCheck()) {
            // RecyclerView
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            // calling Adapter
            adapter = new CardAdapter(getActivity().getApplicationContext(), mainItems);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Интернет байхгүй");
        }
        return view;
    }

    // changeFragment function
    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent,fragment).commit();
    }

    // create CardAdapter
    private class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

        private ArrayList<mainItem> listItems;
        private Context mContext;

        public CardAdapter(Context mContext, ArrayList<mainItem> listItems) {
            this.listItems = listItems;
            this.mContext = mContext;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent,false);
            return new ViewHolder(view, listItems);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txtName.setText("Дүн: "+listItems.get(position).getNameList()+"₮");
            holder.txtAmount.setText("Төлөв: "+listItems.get(position).getId());
            holder.txtDate.setText("Огноо: "+listItems.get(position).getDate());
            holder.optionDigit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext,holder.optionDigit);
                    popupMenu.inflate(R.menu.option_menu);
                    if (holder.txtAmount.getText().equals("12")) {
                        popupMenu.getMenu().getItem(3).setVisible(false);
                    } else {}
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.menu_item_home:
                                    Fragment home = new Home();
                                    changeFragment(home);
                                    getActivity().setTitle(menuItem.getTitle());
                                    break;
                                case R.id.menu_item_hereglee:
                                    Fragment fragment = new Hereglee();
                                    changeFragment(fragment);
                                    getActivity().setTitle(menuItem.getTitle());
                                    break;
                                case R.id.menu_item_cost:
                                    Fragment cost = new Cost();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("invoice_id",listItems.get(position).getId().toString());
                                    Toast.makeText(mContext,listItems.get(position).getId(),Toast.LENGTH_SHORT).show();
                                    cost.setArguments(bundle);
                                    changeFragment(cost);
                                    getActivity().setTitle(menuItem.getTitle());
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        // create ViewHolder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView txtName;
            public TextView txtAmount;
            public TextView txtDate;
            public ImageView optionDigit;
            ArrayList<mainItem> items;
            public ViewHolder(View itemView, ArrayList<mainItem> items) {
                super(itemView);
                this.items = items;
                itemView.setOnClickListener(this);
                txtName = (TextView)itemView.findViewById(R.id.txtName);
                txtAmount = (TextView)itemView.findViewById(R.id.txtamount);
                txtDate = (TextView)itemView.findViewById(R.id.txtdate);
                optionDigit = (ImageView) itemView.findViewById(R.id.txtOptionDigit);
            }
            // Items Click
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                mainItem items = this.items.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("payname",items.getNameList());;
                bundle.putString("payment",paymentList.get(position));

                datetime = new datetime();
                String date = datetime.date(items.getDate());
                bundle.putString("paymentDATE",date);
                String time = datetime.time(items.getDate());
                bundle.putString("paymentTIME",time);
                bundle.putString("sum",items.getNameList());

                // Details Fragment
                DetailsPayment detailsPayment = new DetailsPayment();
                FragmentManager fragmentManager = getFragmentManager();
                detailsPayment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flcontent,detailsPayment).commit();
            }
        }
    }

    // mainItems
    private class mainItem {
        private String nameList;
        private String id;
        private String date;

        public mainItem(String nameList, String id, String date) {
            this.nameList = nameList;
            this.id = id;
            this.date = date;
        }

        public String getNameList() {
            return nameList;
        }

        public void setNameList(String nameList) {
            this.nameList = nameList;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
