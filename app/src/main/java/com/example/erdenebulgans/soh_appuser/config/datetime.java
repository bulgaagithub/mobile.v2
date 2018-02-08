package com.example.erdenebulgans.soh_appuser.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Erdenebulgan.S on 1/7/2018.
 */

public class datetime {

    public String datetime() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Ulaanbaatar");
        sdf.setTimeZone(tz);
        String dateString = sdf.format(date);
        return dateString;
    }

    public String time(String dates) {
        String date = dates;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Ulaanbaatar");
        sdf.setTimeZone(tz);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String newFormat = formatter.format(testDate);
        return newFormat;
    }

    public String date(String date) {
        String input = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Ulaanbaatar");
        sdf.setTimeZone(tz);
        Date testDate = null;
        try {
            testDate = sdf.parse(input);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String newFormat = formatter.format(testDate);
        return newFormat;
    }
}
