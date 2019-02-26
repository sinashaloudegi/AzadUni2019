package com.sinash.azaduni;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CustomListAsatid extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] name;
    private final String[] reshte;
    private final String[] email;
    private final String[] link;
    private final String[] madrak;
    private final String[] daraje;
    private final String[] rezoome;
    private final byte[][] image;
    long enqueue;
    DownloadManager dm;
    String im2, imagePath2;

    public CustomListAsatid(Activity context,
                            String[] name, String[] reshte, String[] email, String[] link, String[] madrak, String[] daraje, String[] rezoome, byte[][] image) {
        super(context, R.layout.listasatid, name);
        this.context = context;
        this.name = name;
        this.image = image;
        this.reshte = reshte;
        this.email = email;
        this.link = link;
        this.madrak = madrak;
        this.daraje = daraje;
        this.rezoome = rezoome;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.listasatid, null, true);
        TextView txtname = (TextView) rowView.findViewById(R.id.textnameasatid);
        TextView txtreshte = (TextView) rowView.findViewById(R.id.textreshte);
        TextView txtmadrak = (TextView) rowView.findViewById(R.id.textmadrak);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageasatid);
        TextView txtdaraje = (TextView) rowView.findViewById(R.id.textdaraje);
        TextView txtemail = (TextView) rowView.findViewById(R.id.textemail);
        Button btnlink = (Button) rowView.findViewById(R.id.btnlink);
        Button btnrezoome = (Button) rowView.findViewById(R.id.btnrezoome);

        txtname.setText(name[position]);

        txtreshte.setText(reshte[position]);
        if (!reshte[position].equals("null")) {
            txtmadrak.setText(madrak[position] + " " + reshte[position]);
        } else {
            txtmadrak.setText(madrak[position]);

        }

        txtdaraje.setText(daraje[position]);


        txtemail.setText(email[position]);

        btnlink.setText("صفحه شخصی");


        btnrezoome.setText("رزومه");
        try {
            byte[] decodedString = new byte[image[position].length];
            for (int i = 0; i < decodedString.length; i++) {
                decodedString[i] = image[position][i];
            }
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                imageView.setImageResource(R.drawable.photoostad);

            } else {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {

            imageView.setImageResource(R.drawable.photoostad);
        }


        btnlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link[position]));
                    context.startActivity(myIntent);
                } catch (Exception e) {

                    Toast.makeText(context, "لینک مورد نظر موجود نیست", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnrezoome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rowView;
    }

    private boolean isInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
