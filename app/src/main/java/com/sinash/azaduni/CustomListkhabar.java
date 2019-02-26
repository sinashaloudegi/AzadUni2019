package com.sinash.azaduni;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListkhabar extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] text;
    private final String[] text_id;
    private final String[] count;


    public CustomListkhabar(Activity context,
                            String[] text, String[] text_id, String[] count) {
        super(context, R.layout.list_khabar, text_id);
        this.context = context;
        this.text = text;
        this.text_id = text_id;
        this.count = count;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_khabar, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textlink2);
        TextView txtId = (TextView) rowView.findViewById(R.id.textlink_id2);
        txtTitle.setText(text[position]);
        txtId.setText(text_id[position]);
        TextView txtcount = (TextView) rowView.findViewById(R.id.txtcount);
        if (!count[position].equals("0")) {
            txtcount.setText(count[position]);
        } else {
            txtcount.setVisibility(View.INVISIBLE);
        }


        return rowView;
    }

}
