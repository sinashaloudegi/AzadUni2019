package com.sinash.azaduni;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class NewsParser {

    Bitmap image;
    Context context;

    public NewsParser(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public List<News> newsParse(String dataString) {

        JSONArray jsonArray = null;
        List<News> allNews;
        try {
            jsonArray = new JSONArray(dataString);

            allNews = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                News news = new News();

                news.setNid(object.getInt("0"));
                news.setType(object.getInt("1"));
                news.setTitle(object.getString("2"));
                news.setText(object.getString("3"));
                news.setImage(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.akhbaricon));
                news.setLink(object.getString("5"));
                news.setCreated_at(object.getString("6"));
                news.setUpdated_at(object.getString("7"));
                news.setImageUp(object.getString("4"));

                // /////////////////////////////////////////////////////////////

                try {
                    URL url = new URL(news.getImageUp());

                    File myDir = new File("/sdcard/azaduni/news/");
                    String fname = news.getTitle() + ".png";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();

					/* Open a connection */
                    URLConnection ucon = url.openConnection();
                    InputStream inputStream = null;
                    HttpURLConnection httpConn = (HttpURLConnection) ucon;
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();

                    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = httpConn.getInputStream();
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    int totalSize = httpConn.getContentLength();
                    int downloadedSize = 0;
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;
                        Log.i("Progress:", "downloadedSize:" + downloadedSize
                                + "totalSize:" + totalSize);
                    }

                    fos.close();
                    Log.d("test", "Image Saved in sdcard..");
                } catch (IOException io) {
                    io.printStackTrace();
                    Log.d("aaaaaaaaaaaaaaaaaaa", io.toString());
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.d("aaaaaaa", e.toString());
                }

                allNews.add(news);

            }
            return allNews;
        } catch (JSONException e) {
            Log.d("JSON ERROR", e.toString());
            return null;
        }

    }

}
