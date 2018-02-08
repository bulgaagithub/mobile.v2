package com.example.erdenebulgans.soh_appuser.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.config.Network;

/**
 * A simple {@link Fragment} subclass.
 */
public class Details extends Fragment {

    private  TextView amountTitles,amountDetails, amounted_dateDetails, apartment;
    private FloatingActionButton floatingActionButton;
    private Network network;
    String titles;
    public Details() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(getActivity().getApplicationContext());
        if (network.networkCheck()) {
        } else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.detailsinfo, container, false);
        amountDetails = (TextView) view.findViewById(R.id.announceDetails);
        amounted_dateDetails = (TextView) view.findViewById(R.id.announcedateDetails);
        apartment = (TextView) view.findViewById(R.id.apartment);
        amountTitles = (TextView) view.findViewById(R.id.txtTitles);

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home home = new Home();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,home).commit();
            }
        });

        if (network.networkCheck()) {
            Bundle bundle = new Bundle();
            bundle = getArguments();
            amountTitles.setText(bundle.getString("title"));
            amountDetails.setText(bundle.getString("announce"));
            amounted_dateDetails.setText("Нийтэлсэн: "+bundle.getString("announce_date"));
            apartment.setText("Байрны №" + bundle.getString("apartment_number"));
            return view;
        } else {
            amountTitles.setText("Интернет байхгүй");
            amountDetails.setVisibility(View.GONE);
            amounted_dateDetails.setVisibility(View.GONE);
            apartment.setVisibility(View.GONE);
            Toast.makeText(getActivity().getApplicationContext(),"Интернет тасарсан байна!",Toast.LENGTH_SHORT).show();
            return view;
        }
    }
}
