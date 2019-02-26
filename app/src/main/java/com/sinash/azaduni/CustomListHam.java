package com.sinash.azaduni;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CustomListHam extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] des;
    private final String[] time;
    private final String[] link;
    private final byte[][] image;


    public CustomListHam(Activity context,
                         String[] des, String[] time, String[] link, byte[][] image) {
        super(context, R.layout.listham, link);
        this.context = context;
        this.des = des;
        this.image = image;
        this.time = time;
        this.link = link;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.listham, null, true);
        TextView txtham = (TextView) rowView.findViewById(R.id.titleham);
        TextView timeham = (TextView) rowView.findViewById(R.id.timeham);
        TextView linkham = (TextView) rowView.findViewById(R.id.linkham);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageham);

        txtham.setText(des[position]);
        timeham.setText(time[position]);
        linkham.setText(link[position]);
        try {
            byte[] decodedString = new byte[image[position].length];
            for (int i = 0; i < decodedString.length; i++) {
                decodedString[i] = image[position][i];
            }
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                imageView.setImageResource(R.drawable.armazaduni);

            } else {
                imageView.setImageBitmap(bitmap);
            }

        } catch (Exception e) {

            imageView.setImageResource(R.drawable.armazaduni);
        }


        return rowView;
    }

}
