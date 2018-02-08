package com.example.erdenebulgans.soh_appuser.fragments;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.Items.AccountItems;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment {

    private SQLite sqLite;
    private Cursor cursor;
    private ArrayList<dataList> dataLists;
    private String defaultData;

    private TextView urgiin_ovog,lastname,firstname,members,register,duureg,
                     khoroo, bair, door, street, khoroolol, mobile, mail,
                     bornplace, size, rooms, cold, warm, electrical, cost;
    private String activityMyData;
    private Network network;

    public Account() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        Main2Activity activity = (Main2Activity) getActivity();
        activityMyData = activity.getMyData();
        dataLists = new ArrayList<>();
        defaultData = "{\"users\":[{\"id\":0,\"gereenii_dugaar\":0,\"urgiin_ovog\":\"urgiin_ovog\",\"ovog\":\"ovog\",\"ner\":\"ner\",\"ambul\":\"ambul\",\"register_id\":\"register\",\"duureg\":\"duureg\",\"khoroo\":\"khoroo\",\"bair\":\"bair\",\"haalga\":\"haalga\",\"gudamj\":\"gudamj\",\"khoroolol\":\"khoroolol\",\"utas\":\"utas\",\"mail\":\"mail\",\"torsongazar\":\"torsonfazar\",\"talbainkhemjee\":\"talbai\",\"orooniitoo\":\"orooniitoo\",\"huiten_us_tooluur\":\"huiten_us_tooluur\",\"haluun_us_tooluur\":\"haluun_us_tooluur\",\"tsahilgaan_tooluur\":\"tsahilgaan_tooluur\",\"tsahilgaan_tariff\":\"tsahilgaan_tariff\"}]}";
        if (network.networkCheck()) {
            if (activityMyData != null ) {
                try {
                    JSONObject jsonObject = new JSONObject(activityMyData);
                    Log.d("json", jsonObject.toString());
                    JSONArray array = jsonObject.getJSONArray("users");
                    Log.d("jsonArray", jsonObject.toString());
                    JSONObject object;
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        dataList data = new dataList(object.getString("urgiin_ovog"), object.getString("ovog"),
                                object.getString("ner"), object.getString("ambul"),
                                object.getString("register_id"), object.getString("duureg"),
                                object.getString("khoroo"), object.getString("bair"),
                                object.getString("haalga"), object.getString("gudamj"),
                                object.getString("khoroolol"), object.getString("utas"),
                                object.getString("mail"), object.getString("torsongazar"),
                                object.getString("talbainkhemjee"), object.getString("orooniitoo"),
                                object.getString("huiten_us_tooluur"), object.getString("haluun_us_tooluur"),
                                object.getString("electric_tooluur"), object.getString("tsahilgaan_tariff"));
                        dataLists.add(data);
                    }
                    Log.d("datalist", "" + dataLists.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Home home = new Home();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,home).commit();
            }
        } else {
            try {
                JSONObject jsonObject = new JSONObject(defaultData);
                Log.d("json", jsonObject.toString());
                JSONArray array = jsonObject.getJSONArray("users");
                Log.d("jsonArray", jsonObject.toString());
                JSONObject object;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    dataList data = new dataList(object.getString("urgiin_ovog"), object.getString("ovog"),
                            object.getString("ner"), object.getString("ambul"),
                            object.getString("register_id"), object.getString("duureg"),
                            object.getString("khoroo"), object.getString("bair"),
                            object.getString("haalga"), object.getString("gudamj"),
                            object.getString("khoroolol"), object.getString("utas"),
                            object.getString("mail"), object.getString("torsongazar"),
                            object.getString("talbainkhemjee"), object.getString("orooniitoo"),
                            object.getString("huiten_us_tooluur"), object.getString("haluun_us_tooluur"),
                            object.getString("electric_tooluur"), object.getString("tsahilgaan_tariff"));
                    dataLists.add(data);
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

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        // ========================================
        urgiin_ovog = (TextView) view.findViewById(R.id.urgiin_ovog);
        lastname = (TextView) view.findViewById(R.id.lastname);
        firstname = (TextView) view.findViewById(R.id.firstname);
        members = (TextView) view.findViewById(R.id.member);
        register = (TextView) view.findViewById(R.id.register);
        bornplace = (TextView) view.findViewById(R.id.bornplace);

        duureg = (TextView) view.findViewById(R.id.duureg);
        khoroo = (TextView) view.findViewById(R.id.khoroo);
        bair = (TextView) view.findViewById(R.id.apartments);
        door = (TextView) view.findViewById(R.id.door);
        street = (TextView) view.findViewById(R.id.street);
        khoroolol = (TextView) view.findViewById(R.id.horoolol);
        mobile = (TextView) view.findViewById(R.id.mobile);
        mail = (TextView) view.findViewById(R.id.email);
        size = (TextView) view.findViewById(R.id.size);
        rooms = (TextView) view.findViewById(R.id.rooms);
        cold = (TextView) view.findViewById(R.id.cold);
        warm = (TextView) view.findViewById(R.id.warm);
        electrical = (TextView) view.findViewById(R.id.electrical);
        cost = (TextView) view.findViewById(R.id.electrical_cost);
        // ======================================== //
        if (network.networkCheck()) {
            // ========================================
            try {
                for (int s = 0; s < 1; s++) {
                    for (int i = 0; i < dataLists.size(); i++) {
                        urgiin_ovog.setText(dataLists.get(i).getUrgiin_ovog());
                        lastname.setText(dataLists.get(i).getLastname());
                        firstname.setText(dataLists.get(i).getFirstname());
                        members.setText(dataLists.get(i).getMembers());
                        register.setText(dataLists.get(i).getRegiter());
                        bornplace.setText(dataLists.get(i).getBornplace());

                        duureg.setText(dataLists.get(i).getDuureg());
                        khoroo.setText(dataLists.get(i).getKhoroo());
                        bair.setText(dataLists.get(i).getBair());
                        door.setText(dataLists.get(i).getDoor());
                        street.setText(dataLists.get(i).getStreet());
                        khoroolol.setText(dataLists.get(i).getKhoroolol());
                        mobile.setText(dataLists.get(i).getMobile());
                        mail.setText(dataLists.get(i).getMail());
                        size.setText(dataLists.get(i).getSize());
                        rooms.setText(dataLists.get(i).getRooms());
                        cold.setText(dataLists.get(i).getCold());
                        warm.setText(dataLists.get(i).getWarm());
                        electrical.setText(dataLists.get(i).getElectrical());
                        cost.setText(dataLists.get(i).getCost());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //=========================================//

        } else {
            // ========================================
            try {
                for (int s = 0; s < 1; s++) {
                    for (int i = 0; i < dataLists.size(); i++) {
                        urgiin_ovog.setText("Ургийн овог: " + dataLists.get(i).getUrgiin_ovog());
                        lastname.setText("Овог: " + dataLists.get(i).getLastname());
                        firstname.setText("Нэр: " + dataLists.get(i).getFirstname());
                        members.setText("Ам бүл: " + dataLists.get(i).getMembers());
                        register.setText("Регистр: " + dataLists.get(i).getRegiter());
                        bornplace.setText("Төрсөн газар: " + dataLists.get(i).getBornplace());

                        duureg.setText("Дүүрэг: " + dataLists.get(i).getDuureg());
                        khoroo.setText("Хороо: " + dataLists.get(i).getKhoroo());
                        bair.setText("Байр: " + dataLists.get(i).getBair());
                        door.setText("Хаалга: " + dataLists.get(i).getDoor());
                        street.setText("Гудамж: " + dataLists.get(i).getStreet());
                        khoroolol.setText("Хороолол: " + dataLists.get(i).getKhoroolol());
                        mobile.setText("Утас: " + dataLists.get(i).getMobile());
                        mail.setText("И-мэйл: " + dataLists.get(i).getMail());
                        size.setText("Талбайн хэмжээ: " + dataLists.get(i).getSize());
                        rooms.setText("Өрөөний тоо: " + dataLists.get(i).getRooms());
                        cold.setText("Хүйтэн усны тоолуур: " + dataLists.get(i).getCold());
                        warm.setText("Халуун усны тоолуур: " + dataLists.get(i).getWarm());
                        electrical.setText("Цахилгаан тоолуур: " + dataLists.get(i).getElectrical());
                        cost.setText("Цахилгаан тариф: " + dataLists.get(i).getCost());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // ========================================= //
            Toast.makeText(getActivity().getApplicationContext(),"Интернет тасарсан байна!",Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public class dataList{
       String urgiin_ovog,lastname,firstname,members,regiter,duureg,
                khoroo, bair, door, street, khoroolol, mobile, mail,
                bornplace, size, rooms, cold, warm, electrical, cost;

        public dataList(String urgiin_ovog, String lastname, String firstname, String members, String regiter,
                        String duureg, String khoroo, String bair, String door, String street, String khoroolol,
                        String mobile, String mail, String bornplace, String size, String rooms, String cold,
                        String warm, String electrical, String cost) {
            this.urgiin_ovog = urgiin_ovog;
            this.lastname = lastname;
            this.firstname = firstname;
            this.members = members;
            this.regiter = regiter;
            this.duureg = duureg;
            this.khoroo = khoroo;
            this.bair = bair;
            this.door = door;
            this.street = street;
            this.khoroolol = khoroolol;
            this.mobile = mobile;
            this.mail = mail;
            this.bornplace = bornplace;
            this.size = size;
            this.rooms = rooms;
            this.cold = cold;
            this.warm = warm;
            this.electrical = electrical;
            this.cost = cost;
        }

        public String getUrgiin_ovog() {
            return urgiin_ovog;
        }

        public void setUrgiin_ovog(String urgiin_ovog) {
            this.urgiin_ovog = urgiin_ovog;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getMembers() {
            return members;
        }

        public void setMembers(String members) {
            this.members = members;
        }

        public String getRegiter() {
            return regiter;
        }

        public void setRegiter(String regiter) {
            this.regiter = regiter;
        }

        public String getDuureg() {
            return duureg;
        }

        public void setDuureg(String duureg) {
            this.duureg = duureg;
        }

        public String getKhoroo() {
            return khoroo;
        }

        public void setKhoroo(String khoroo) {
            this.khoroo = khoroo;
        }

        public String getBair() {
            return bair;
        }

        public void setBair(String bair) {
            this.bair = bair;
        }

        public String getDoor() {
            return door;
        }

        public void setDoor(String door) {
            this.door = door;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getKhoroolol() {
            return khoroolol;
        }

        public void setKhoroolol(String khoroolol) {
            this.khoroolol = khoroolol;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getBornplace() {
            return bornplace;
        }

        public void setBornplace(String bornplace) {
            this.bornplace = bornplace;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getRooms() {
            return rooms;
        }

        public void setRooms(String rooms) {
            this.rooms = rooms;
        }

        public String getCold() {
            return cold;
        }

        public void setCold(String cold) {
            this.cold = cold;
        }

        public String getWarm() {
            return warm;
        }

        public void setWarm(String warm) {
            this.warm = warm;
        }

        public String getElectrical() {
            return electrical;
        }

        public void setElectrical(String electrical) {
            this.electrical = electrical;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }
    }
}
