package com.sinash.azaduni;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListSearch extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] nam;
    private final String[] num;


    public CustomListSearch(Activity context,
                            String[] nam, String[] num) {
        super(context, R.layout.custom_list_search, num);
        this.context = context;
        this.nam = nam;
        this.num = num;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_search, null, true);

        TextView txtnam = (TextView) rowView.findViewById(R.id.namcu);
        TextView txtnum = (TextView) rowView.findViewById(R.id.numcu);
        txtnam.setText(nam[position]);
        txtnum.setText(num[position]);
        return rowView;
    }

}
