package com.sinash.azaduni;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] web;
    private final Integer[] imageId;
    private final String[] count;


    public CustomList(Activity context,
                      String[] web, Integer[] imageId, String[] count) {
        super(context, R.layout.listmenu, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.count = count;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listmenu, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textlist10);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView4);

        txtTitle.setText(web[position]);
        imageView.setBackgroundResource(imageId[position]);
        TextView txtcount = (TextView) rowView.findViewById(R.id.textView17);
        txtcount.setText(count[position]);
        if (!count[position].equals("n")) {

            txtcount.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        } else {

            txtcount.setVisibility(View.GONE);
        }

        return rowView;
    }

}
