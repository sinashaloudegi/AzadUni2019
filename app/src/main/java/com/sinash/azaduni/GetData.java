package com.sinash.azaduni;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GetData {

    public String getData1(String link) {
        BufferedReader reader = null;
        String sb = null;

        try {

            // //////////////////

            URL url = new URL(link);
            Log.d("te", "te");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            Log.d("te", "te1");

            // ///////////////

            reader = new BufferedReader(new InputStreamReader(
                    http.getInputStream()));
            Log.d("te", "te2");
            String line;
            while ((line = reader.readLine()) != null) {
                sb = line;

                Log.d("te", "te3");

            }
            // float f=Float.parseFloat(sb);
            Log.d("sssssssss", sb);

            //Log.d("KKK", String.valueOf(sb.charAt(2)));
            String reString = sb.substring(1);

            Log.d("te", "te");
            return sb;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception", e.toString());
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                    Log.d("te", "te8");

                } catch (Exception e2) {
                    return null;
                }
        }
    }

    public String getData3(String link, String clientmax) {

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            String clietmax2send = URLEncoder.encode("news", "UTF8") + "=" + URLEncoder.encode(String.valueOf(clientmax), "UTF8");
            Log.e("clientSend", clietmax2send);
            // ////////////////////////////////////////////////

            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // ////////////////////////////////////////////////

            http.setDoOutput(true);
            OutputStreamWriter streamWriter = new OutputStreamWriter(
                    http.getOutputStream());
            streamWriter.write(clietmax2send);

            streamWriter.flush();

            reader = new BufferedReader(new InputStreamReader(
                    http.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }

            return sb.toString();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return e.toString();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e2) {
                    return null;
                }
        }
    }

    public String getData2(String link, String clientmax) {

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            String clietmax2send = URLEncoder.encode("version", "UTF8") + "=" + URLEncoder.encode(String.valueOf(clientmax), "UTF8");
            Log.e("clientSend", clietmax2send);
            // ////////////////////////////////////////////////

            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // ////////////////////////////////////////////////

            http.setDoOutput(true);
            OutputStreamWriter streamWriter = new OutputStreamWriter(
                    http.getOutputStream());
            streamWriter.write(clietmax2send);

            streamWriter.flush();

            reader = new BufferedReader(new InputStreamReader(
                    http.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }

            return sb.toString();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return e.toString();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e2) {
                    return null;
                }
        }
    }

    public String getData(String link, int clientmax) {

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            String clietmax2send = URLEncoder.encode("id", "UTF8") + "="
                    + URLEncoder.encode(String.valueOf(clientmax), "UTF8");

            // ////////////////////////////////////////////////

            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // ////////////////////////////////////////////////

            http.setDoOutput(true);
            OutputStreamWriter streamWriter = new OutputStreamWriter(
                    http.getOutputStream());
            streamWriter.write(clietmax2send);

            streamWriter.flush();

            reader = new BufferedReader(new InputStreamReader(
                    http.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }

            return sb.toString();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return e.toString();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e2) {
                    return null;
                }
        }
    }

}
