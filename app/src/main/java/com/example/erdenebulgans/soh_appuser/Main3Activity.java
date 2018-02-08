package com.example.erdenebulgans.soh_appuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erdenebulgans.soh_appuser.R;
import com.example.erdenebulgans.soh_appuser.config.Network;
import com.example.erdenebulgans.soh_appuser.database.SQLite;
import com.example.erdenebulgans.soh_appuser.fragments.Home;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {

    private Network network;
    private SQLite sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        network = new Network(getApplicationContext());
        sqLite = new SQLite(getApplicationContext());
        int row = sqLite.numberOfRows();
        TextView title = (TextView)findViewById(R.id.titlelabel);
        TextView announce = (TextView)findViewById(R.id.details);
        TextView apart = (TextView)findViewById(R.id.apart);
        TextView ognoo = (TextView)findViewById(R.id.activityDate);
        Bundle bundle = getIntent().getExtras();
        if (row>0) {
            if (network.networkCheck()) {
                title.setText(bundle.getString("title"));
                announce.setText(bundle.getString("announce"));
                ognoo.setText("Нийтэлсэн: " + bundle.getString("announce_date"));
                apart.setText("Байрны №" + bundle.getString("apartment_number"));
            } else {
                title.setText("Интернет тасарсан байна.");
                announce.setVisibility(View.GONE);
                ognoo.setVisibility(View.GONE);
                apart.setVisibility(View.GONE);
            }
        } else {
            startActivity(new Intent(this,Main2Activity.class));
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
