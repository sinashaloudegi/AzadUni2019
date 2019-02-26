package com.sinash.azaduni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SinaSh on 04/03/2016.
 */
public class ImageDownloader {
    Context context;
    String url;
    String table;
    String columnName;
    String where;

    DataBaseHelper mDb = null;
    SQLiteDatabase m = null;
    DataBaseHelper dbHelperSina = null;

    public ImageDownloader(Context context, String url, String table, String columnName, String where) {
        this.context = context;
        this.url = url;
        this.table = table;
        this.columnName = columnName;
        this.where = where;
        mDb = new DataBaseHelper(context);
        m = mDb.getReadableDatabase();
        dbHelperSina = new DataBaseHelper(context);
    }

    public void download() {

        BackTask task = new BackTask();
        task.execute(url);

    }


    // AsynnTask to run download an image in background
    private class BackTask extends AsyncTask<String, Void, Bitmap> {
        protected void onPreExecute() {
            try {
                dbHelperSina.createDataBase();
                dbHelperSina.openDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                Log.e("hat", "1111111111111111111");

                // Download the image
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                // Decode image to get smaller image to save memory
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeStream(is, null, options);
                is.close();
                Log.e("hat", "2222222222222");
            } catch (IOException e) {
                return null;
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            Log.e("Result", result + "");
            if (result != null) {
                dbHelperSina.insertBitmap(result, columnName, table, where);
            }

        }
    }

}
