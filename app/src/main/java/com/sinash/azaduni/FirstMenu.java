package com.sinash.azaduni;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class FirstMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_menu);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#22497d")));

        LinearLayout sc = (LinearLayout) findViewById(R.id.sc);
        YoYo.with(Techniques.FadeIn).duration(2600).playOn(sc);

    }

    public void Onclickd1(View view) {
        Intent goToMain = new Intent(FirstMenu.this, MainPage.class);
        startActivity(goToMain);

    }

    public void Onclickd2(View view) {
        Intent goToRiasat = new Intent(FirstMenu.this, Riasat.class);
        startActivity(goToRiasat);
    }

    public void Onclickd3(View view) {
        Intent goToMoavenat = new Intent(FirstMenu.this, Moavenat.class);
        startActivity(goToMoavenat);
    }

    public void Onclickd4(View view) {
        Intent goToDaneshkade = new Intent(FirstMenu.this, Daneshkade.class);
        startActivity(goToDaneshkade);
    }

    public void Onclickd5(View view) {
        Intent goToAbout = new Intent(FirstMenu.this, About.class);
        startActivity(goToAbout);
    }

    public void Onclickd6(View view) {
        Intent goToPazhohesh = new Intent(FirstMenu.this, Pazhoheshkadeh.class);
        startActivity(goToPazhohesh);
    }

    public void Onclickd7(View view) {
        Intent goToAz = new Intent(FirstMenu.this, AzmayeshGahHa.class);
        startActivity(goToAz);
    }

    public void Onclickd8(View view) {
        Intent goToHam = new Intent(FirstMenu.this, HamayeshVaMosabeghat.class);
        startActivity(goToHam);
    }

    public void Onclickd9(View view) {
        Intent goToAkhbar = new Intent(FirstMenu.this, NewsList.class);
        startActivity(goToAkhbar);
    }

    public void Onclickd10(View view) {
        Intent goToTelefon = new Intent(FirstMenu.this, Phone.class);
        startActivity(goToTelefon);

    }

    public void Onclickd11(View view) {
        Intent goToLink = new Intent(FirstMenu.this, Link.class);
        goToLink.putExtra("ForL", "L");
        startActivity(goToLink);

    }

    public void Onclickd12(View view) {
        if (isInternet()) {
            Intent goToUpdate = new Intent(FirstMenu.this, Update.class);
            startActivity(goToUpdate);
        } else {
            Toast.makeText(FirstMenu.this, "لطفا به اینترنت متصل شوید", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) FirstMenu.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
