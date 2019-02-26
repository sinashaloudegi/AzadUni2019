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

public class CustomListDaneshkade extends ArrayAdapter<String> {


    private final Activity context;

    private final String[] text;
    private final String[] text_id;
    private final byte[][] image;


    public CustomListDaneshkade(Activity context,
                                String[] text, String[] text_id, byte[][] image) {
        super(context, R.layout.list, text_id);
        this.context = context;
        this.text = text;
        this.image = image;
        this.text_id = text_id;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textlist);
        TextView txtId = (TextView) rowView.findViewById(R.id.text_id);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imagelist);

        txtTitle.setText(text[position]);
        txtId.setText(text_id[position]);
        try {
            byte[] decodedString = new byte[image[position].length];
            for (int i = 0; i < decodedString.length; i++) {
                decodedString[i] = image[position][i];
            }
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {

                imageView.setImageResource(R.drawable.photodaneshkade);

            } else {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {

            imageView.setImageResource(R.drawable.photodaneshkade);
        }


        return rowView;
    }

}
