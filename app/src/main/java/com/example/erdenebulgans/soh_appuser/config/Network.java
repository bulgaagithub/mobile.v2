package com.example.erdenebulgans.soh_appuser.config;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Erdenebulgan.S on 1/10/2018.
 */

public class Network {

    Context context;

    public Network(Context context) {
        this.context = context;
    }

    public boolean networkCheck()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return true;
            }
        }
        return false;
    }
}
