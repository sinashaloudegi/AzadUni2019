package com.sinash.azaduni;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CustomListAkhbar extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] matn;
    private final String[] id;
    private final String[] tag;
    private final String[] date;
    private final byte[][] image;
    private final String[] seen;


    public CustomListAkhbar(Activity context,
                            String[] matn, String[] id, String[] tag, String[] date, byte[][] image, String[] seen) {
        super(context, R.layout.listakhbar, id);
        this.context = context;
        this.matn = matn;
        this.image = image;
        this.id = id;
        this.tag = tag;
        this.date = date;
        this.seen = seen;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.listakhbar, null, true);
        TextView txtmatn = (TextView) rowView.findViewById(R.id.textmatn);
        TextView txtid = (TextView) rowView.findViewById(R.id.textid);
        TextView txttag = (TextView) rowView.findViewById(R.id.texttag);
        TextView txtdate = (TextView) rowView.findViewById(R.id.textdate);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageakhbar);
        LinearLayout l = (LinearLayout) rowView.findViewById(R.id.seeeen);
        if (seen[position].equals("1")) {
            l.setBackgroundColor(Color.WHITE);
        } else {

            l.setBackgroundColor(Color.parseColor("#ffd2eadf"));
        }

        txtmatn.setText(matn[position]);
        txtdate.setText(date[position]);
        txttag.setText(tag[position]);
        txtid.setText(id[position]);
        try {


            byte[] decodedString = new byte[image[position].length];
            for (int i = 0; i < decodedString.length; i++) {
                decodedString[i] = image[position][i];
            }
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {

                imageView.setImageResource(R.drawable.akhbaricon);
            } else {

                imageView.setImageBitmap(bitmap);
            }

        } catch (Exception e) {

            imageView.setImageResource(R.drawable.akhbaricon);
        }


        return rowView;
    }

}
