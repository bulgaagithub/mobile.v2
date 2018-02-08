package com.example.erdenebulgans.soh_appuser.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.config.Network;
import com.example.erdenebulgans.soh_appuser.database.SQLite;

import java.sql.SQLData;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cost extends Fragment {

    private WebView webView;
    private FloatingActionButton floatButton;
    private TextView textVie;
    private Network network;
    private SQLite sqLite;
    private Cursor cursor;
    private String url;

    public Cost() {
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
        View view = inflater.inflate(R.layout.fragment_cost, container, false);
        webView = (WebView) view.findViewById(R.id.webView);
        textVie = (TextView)view.findViewById(R.id.networkFalse);
        floatButton = (FloatingActionButton)view.findViewById(R.id.backWebView);
        // click Listener
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment tulbur = new Tulbur();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,tulbur).commit();
            }
        });
        sqLite = new SQLite(getActivity().getApplicationContext());
        Bundle bundle = getArguments();
        if (network.networkCheck()) {
            cursor = sqLite.sessionSelect();
            cursor.moveToNext();
            String username = cursor.getString(cursor.getColumnIndex("uid"));
            String password = cursor.getString(cursor.getColumnIndex("upassword"));
            String invoice_id = bundle.getString("invoice_id");
            if (!cursor.isClosed()) {
                cursor.close();
            }
            // http://www.voipnice.com/android/bill?username="+username+"&password="+password
            url = "http://www.voipnice.com/android/bill?username="+username+"&password="+password+"&invoice_id="+invoice_id;
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
            textVie.setVisibility(View.GONE);
        } else {
            textVie.setText("Интернет тасарсан байна!");
            textVie.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private class MyBrowser extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
