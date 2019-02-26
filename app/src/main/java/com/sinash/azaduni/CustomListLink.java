package com.sinash.azaduni;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListLink extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] text;
    private final String[] text_id;


    public CustomListLink(Activity context,
                          String[] text, String[] text_id) {
        super(context, R.layout.list_link, text_id);
        this.context = context;
        this.text = text;
        this.text_id = text_id;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_link, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textlink);
        TextView txtId = (TextView) rowView.findViewById(R.id.textlink_id);
        txtTitle.setText(text[position]);
        txtId.setText(text_id[position]);


        return rowView;
    }

}
