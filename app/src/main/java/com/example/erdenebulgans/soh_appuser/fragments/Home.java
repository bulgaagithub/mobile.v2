package com.example.erdenebulgans.soh_appuser.fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.Main2Activity;
import com.example.erdenebulgans.soh_appuser.Main3Activity;
import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.config.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<Items> dataList;
    private CardAdapter cardAdapter;
    private NotificationManager notificationManager;
    private Network network;
    public String myData;
    private Items items;
    private int count = 0;
    // Required empty public constructor
    public Home() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Main2Activity activity = (Main2Activity)getActivity();
        myData = activity.homeData;
        activity.CloseDrawer();
        dataList = new ArrayList<>();
        if (network.networkCheck()) {   // Network true
            if (myData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(myData);
                    JSONArray array = jsonObject.getJSONArray("announce");
                    JSONObject object;
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        items = new Items(object.getString("garchig"), object.getString("zarlal"), object.getString("announced_date"), object.getString("bairnii_dugaar"), object.getString("temdeglegee"));
                        dataList.add(items);
                    }
                    // NOTIFICATION гаргах давталт
//                    for (int x = 0; x < dataList.size(); x++) {
//                        if (dataList.get(x).getNottrue().equals("1")) {
//                            showNotification(x, dataList.get(x).getTitle(), dataList.get(x).getTitle(), dataList.get(x).getAnnounce(), dataList.get(x).getApartment_number(), dataList.get(x).getAnnounced_date());
//                        } else {}
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Fragment fragment = new Fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,fragment).commit();
                activity.recreate();
            }
        } else { // Network false;
            myData = "{\"announce\":[{\"zarlal_id\":1,\"type\":\"type\",\"bairnii_dugaar\":\"интернет байхгүй\",\"user\":\"user\",\"announced_date\":\"интернет байхгүй\",\"garchig\":\"интернет байхгүй\",\"zarlal\":\"zarlal\",\"temdeglegee\":\"temdeglegee\"}]}";
            try {
                JSONObject jsonObject = new JSONObject(myData);
                JSONArray array = jsonObject.getJSONArray("announce");
                JSONObject object;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    Items items = new Items(object.getString("garchig"),object.getString("zarlal"),object.getString("announced_date"),object.getString("bairnii_dugaar"),object.getString("temdeglegee"));
                    dataList.add(items);
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
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.informations);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            cardAdapter = new CardAdapter(dataList, getActivity().getApplicationContext());
            recyclerView.setAdapter(cardAdapter);
            return view;
    }

    // create CardAdapter
    private class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        private ArrayList<Items> listItems;
        private Context context;

        public CardAdapter(ArrayList<Items> listItems, Context context) {
            this.listItems = listItems;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
            return new ViewHolder(view,listItems);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Items items = listItems.get(position);
            holder.announce.setText(items.getTitle());
            holder.announced_date.setText(items.getAnnounced_date());
            // holder.apartment_number.setText("Байрны №"+items.getApartment_number());
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView announce;
            private TextView announced_date;
            private TextView apartment_number;
            ArrayList<Items> items;
            public ViewHolder(View itemView, ArrayList<Items>items) {
                super(itemView);
                this.items = items;
                itemView.setOnClickListener(this);
                announce = (TextView)itemView.findViewById(R.id.announce);
                announced_date = (TextView)itemView.findViewById(R.id.announced_date);
                apartment_number = (TextView)itemView.findViewById(R.id.apartment_number);
            }

            @Override
            public void onClick(View view) {

                //=========
                int position = getAdapterPosition();
                Items items = this.items.get(position);

                //=========
                Bundle bundle = new Bundle();
                bundle.putString("title",items.getTitle());
                bundle.putString("announce",items.getAnnounce());
                bundle.putString("announce_date",items.getAnnounced_date());
                bundle.putString("apartment_number",items.getApartment_number());

                //=========
                Details details = new Details();
                FragmentManager fragmentManager = getFragmentManager();
                details.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flcontent,details).commit();
            }
        }
    }

    // Items
    public class Items {
        String title;
        String announce;
        String announced_date;
        String apartment_number;
        String nottrue;

        public Items(String title, String announce, String announced_date, String apartment_number,String nottrue) {
            this.title = title;
            this.announce = announce;
            this.announced_date = announced_date;
            this.apartment_number = apartment_number;
            this.nottrue = nottrue;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAnnounce() {
            return announce;
        }

        public void setAnnounce(String announce) {
            this.announce = announce;
        }

        public String getAnnounced_date() {
            return announced_date;
        }

        public void setAnnounced_date(String announced_date) {
            this.announced_date = announced_date;
        }

        public String getApartment_number() {
            return apartment_number;
        }

        public void setApartment_number(String apartment_number) {
            this.apartment_number = apartment_number;
        }

        public String getNottrue() {
            return nottrue;
        }

        public void setNottrue(String nottrue) {
            this.nottrue = nottrue;
        }
    }

    // showNotification
    public void showNotification(int id, String label, String title, String news, String apartment, String publish) {
        Intent intent = new Intent(getActivity(),Main3Activity.class);
        intent.putExtra("title",title);
        intent.putExtra("announce",news);
        intent.putExtra("apartment_number",apartment);
        intent.putExtra("announce_date",publish);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity().getApplicationContext());
        taskStackBuilder.addParentStack(Main3Activity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pIntent = taskStackBuilder.getPendingIntent(id,PendingIntent.FLAG_UPDATE_CURRENT);
        count++;
        Notification.Builder notification = new Notification.Builder(getActivity().getApplicationContext())
                .setTicker("Шинэ мэдээ ирлээ.")
                .setSound(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.notification48))
                .setContentTitle(label)
                .setAutoCancel(true)
                .setContentText("Шинэ мэдээ ирсэн байна.")
                .setSmallIcon(R.drawable.ic_library_books_black_24dp)
                .setNumber(count)
                .setContentIntent(pIntent);
        notificationManager.notify(id,notification.build());
    }
}
