package com.example.erdenebulgans.soh_appuser.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.Main2Activity;
import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.config.Network;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Hereglee extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Network network;
    private Main2Activity activity;
    private String checkData;

    // Required empty public constructor
    public Hereglee() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (Main2Activity)getActivity();
        checkData = activity.usageData;
        network = new Network(getActivity().getApplicationContext());
        if (network.networkCheck()) {
            if (checkData != null) {

            } else {
                Home home = new Home();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,home).commit();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(),"Интернет тасарсан байна!",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_hereglee, container, false);
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            return view;
    }

    // setupViewPager function
    private  void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Fragment1(),"Хүйтэн ус");
        adapter.addFragment(new Fragment2(),"Халуун ус");
        adapter.addFragment(new Fragment3(),"Цахилгаан");
        viewPager.setAdapter(adapter);
    }

    // ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentPagerAdapter{

        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentListTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String string){
            mFragmentList.add(fragment);
            mFragmentListTitle.add(string);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentListTitle.get(position);
        }
    }
}
