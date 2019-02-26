package com.sinash.azaduni;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;


public class AzadUniversity extends Activity {

    private boolean isInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_azad_university);
        android.app.ActionBar actionBar = getActionBar();
        actionBar.hide();
        DataBaseHelper mdb = new DataBaseHelper(AzadUniversity.this);
        try {
            mdb.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mdb.openDataBase();

        if (isInternet()) {
            try {
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(AzadUniversity.this, MyService.class);
                PendingIntent pintent = PendingIntent.getService(AzadUniversity.this, 0, intent, 0);
                AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60 * 1000, pintent);
                //   Async checkVer = new Async();
                //  checkVer.execute();
            } catch (Exception e) {
                Toast.makeText(AzadUniversity.this, "لطفا به اینترنت متصل شوید!", Toast.LENGTH_LONG).show();

            }
        }
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animationFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadesplash);
        image.startAnimation(animationFade);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(AzadUniversity.this, FirstMenu.class);
                AzadUniversity.this.startActivity(mainIntent);
                AzadUniversity.this.finish();
            }
        }, 1600);

    }

    public class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            GetData gd = new GetData();
            String result = gd.getData1("http://e-sna.net/iausdj2/json/version.php");
            int resultInt = Integer.parseInt(result.trim());
            Log.e("res", result);
            if (resultInt == 1) {
                new AlertDialog.Builder(AzadUniversity.this)
                        .setTitle("نسخه ی جدید")
                        .setMessage(" نسخه ی جدید نرم افزار موجو است!")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Whatever...
                            }
                        }).create().show();
            }
            return null;
        }
    }

}
