package com.sinash.azaduni;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CustomListPhone extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] name;
    private final String[] semat;
    private final String[] tell;
    private final byte[][] image;
    private final String[] link;

    public CustomListPhone(Activity context,
                           String[] name, String[] semat, String[] tell, byte[][] image, String[] link) {
        super(context, R.layout.listphone, link);
        this.context = context;
        this.name = name;
        this.image = image;
        this.semat = semat;
        this.tell = tell;
        this.link = link;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.listphone, null, true);
        TextView txtname = (TextView) rowView.findViewById(R.id.textnamephon);
        TextView txtsemat = (TextView) rowView.findViewById(R.id.textsemat);
        TextView txttell = (TextView) rowView.findViewById(R.id.texttellphon);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imagephon);
        Button btnlink = (Button) rowView.findViewById(R.id.btnlink2);
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

        txtname.setText(name[position]);
        txtsemat.setText(semat[position]);
        try {
            if (tell[position].isEmpty()) {
                txttell.setText("ندارد");

            } else {
                if (!tell[position].equals("null")) {
                    txttell.setText("+98 " + tell[position]);
                } else {

                    txttell.setText("ندارد");
                }

            }

        } catch (Exception e) {

            txttell.setText("ندارد");
        }
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
        final Button btn = (Button) rowView.findViewById(R.id.buttoncall);
        try {
            if (tell[position].isEmpty()) {

                btn.setActivated(false);
            } else {

                YoYo.with(Techniques.Shake).duration(1600).playOn(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Uri number = Uri.parse("tel:+98" + tell[position]);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        context.startActivity(callIntent);
                    }
                });
            }
        } catch (Exception e) {

            btn.setActivated(false);
        }

        return rowView;
    }

}
