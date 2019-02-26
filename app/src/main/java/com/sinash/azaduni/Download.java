package com.sinash.azaduni;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Download {


    Context context;
    String Url;
    private long enqueue;
    private DownloadManager dm;
    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url;
    private String im2;
    private String imagePath2;


    private boolean isInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void makefolder() {

        File folder = new File(Environment.getExternalStorageDirectory() + "azad");
        boolean success = false;
        if (!folder.exists()) {
            System.out.println("hat");
            success = folder.mkdirs();

        }
        if (!success) {
            System.out.println("hat2");
        } else {
            System.out.println("hat3");
        }


        file_url = Url;
        im2 = Url;
        imagePath2 = "/azad/";
        File folder3 = new File(Environment.getExternalStorageDirectory() + imagePath2 + im2);
        boolean success3 = false;
        if (!folder3.exists()) {
            if (isInternet()) {

                downloadAndOpenPDF();
            } else {

                Toast.makeText(context, "لطفا به اینترنت متصل شوید", Toast.LENGTH_LONG).show();
            }
            success3 = true;
        } else if (!success3) {
            //// show pdf

            Uri path = Uri.fromFile(folder3);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {

        }


        // Image view to show image after downloading

        /**
         * Show Progress bar click event
         * */


    }

    public Download(Context context, String Url) {
        this.context = context;
        this.Url = Url;
        makefolder();
    }


    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {
                Uri path = Uri.fromFile(downloadFile());
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(context,"PDF Reader application is not installed in your device",Toast.LENGTH_LONG).show();
                }
            }
        }).start();

    }

    File downloadFile() {
        File file = null;
        try {

            URL url = new URL(file_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(Environment.getExternalStorageDirectory().toString() + imagePath2 + im2);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            int totalsize = urlConnection.getContentLength();
            Toast.makeText(context, "Starting PDF download...", Toast.LENGTH_LONG).show();

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;
            int downloadedSize = 0;
            float per = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);

                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                Toast.makeText(context, "Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete", Toast.LENGTH_SHORT);
            }
            // close the output stream when complete //
            fileOutput.close();
            Toast.makeText(context, "Download Complete. Open PDF Application installed in the device.", Toast.LENGTH_LONG).show();

        } catch (final MalformedURLException e) {
            System.out.println("1Some error occured. Press back and try again.");
        } catch (final IOException e) {
            System.out.println("2Some error occured. Press back and try again.");
        } catch (final Exception e) {
            System.out.println("Failed to download image. Please check your internet connection.");
        }
        return file;
    }

}