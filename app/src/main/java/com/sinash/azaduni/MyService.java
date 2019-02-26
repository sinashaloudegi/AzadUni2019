package com.sinash.azaduni;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyService extends Service {


    Intent intent1;

    @Override
    public IBinder onBind(Intent arg0) {
        this.intent1 = arg0;

        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent2, int flags, int startId) {
        // Let it continue running until it is stopped.

        ////////////////////////

        try {
            this.intent1 = intent2;
            AsyncCopy ac = new AsyncCopy();
            ac.execute();
        } catch (Exception e) {

        }
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private class AsyncCopy extends AsyncTask<Void, Void, Void> {


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void Notify(String notificationTitle, String notificationMessage, int notificationId, Intent intent) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            @SuppressWarnings("deprecation")
            Intent notificationIntent1 = new Intent(MyService.this, JoziyateKhabar.class);
            notificationIntent1.putExtra("idkhabar", notificationId + "");
            notificationIntent1.putExtra("tbl", "tbl_push");
            Intent reciverIntent = new Intent(MyService.this, MyReceiver.class);
            Notification.Builder builder = new Notification.Builder(MyService.this);

            PendingIntent pIntent = PendingIntent.getActivity(MyService.this, (int) System.currentTimeMillis(), notificationIntent1, 0);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(MyService.this, 0, reciverIntent, 0);

            Notification n = new Notification.Builder(MyService.this)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationMessage)
                    .setSmallIcon(R.drawable.ic_stat_name3)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true).setSound(Uri.parse("android.resource://"
                            + MyService.this.getPackageName() + "/" + R.raw.no2))
                    .build();
            Notification notification = new Notification(R.drawable.iconno, "اطلاعیه ی مهم", System.currentTimeMillis());

            Intent notificationIntent = new Intent(MyService.this, NewsList.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, notificationIntent, 0);

            notification.setLatestEventInfo(getBaseContext(), notificationTitle, notificationMessage, pendingIntent);
            notificationManager.notify(notificationId, n);
            builder.setDeleteIntent(pendingIntent2);

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Void doInBackground(Void... arg0) {


            DataBaseHelper mDb = new DataBaseHelper(MyService.this);
            SQLiteDatabase m = mDb.getReadableDatabase();
            ////SELECT
            String noteQuery = "SELECT * FROM tbl_push WHERE isChecked = 0";
            String chQuery = "SELECT * FROM tbl_push WHERE isChecked = 1";

            Cursor cursor = m.rawQuery(noteQuery, null);
            Cursor chCursor = m.rawQuery(chQuery, null);

            Log.e("size1", cursor.getCount() + "");


            int param = 0;
            if (chCursor.getCount() != 0) {
                for (int i = 0; i < chCursor.getCount(); i++) {
                    chCursor.moveToNext();
                }
                param = chCursor.getInt(chCursor.getColumnIndex("nid"));
                Log.e("size", param + "");
            }

            GetData gd = new GetData();
            int nid;
            int type;
            String title;
            String text;

            try {
                JSONObject noteNews = new JSONObject(gd.getData3("http://e-sna.net/iausdj2/public/api/news", param + ",1"));
                Log.e("JSON", noteNews + "");

                JSONArray dataJson = new JSONArray(noteNews.getString("data"));
                for (int i = 0; i < dataJson.length(); i++) {
                    nid = dataJson.getJSONObject(i).getInt("nid");
                    type = dataJson.getJSONObject(i).getInt("type");
                    title = dataJson.getJSONObject(i).getString("title");
                    text = dataJson.getJSONObject(i).getString("text");


                    m.execSQL("INSERT INTO tbl_push(nid,type,title,text)\n" +
                            "VALUES ('" + nid + "','" + type + "','" + title + "','" + text + "')");

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            cursor.moveToNext();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (intent1 != null) {
                final TestAdapter mDbHelper = new TestAdapter(MyService.this);
                mDbHelper.createDatabase();
                mDbHelper.open();
                Cursor c = mDbHelper.getTestData("*", "tbl_push", "isChecked", "0");
                try {
                    Notify(c.getString(2), "خواندن کامل متن...", c.getInt(0), intent1);
                    String pushChecked = "UPDATE tbl_push SET isChecked = 1 " + " WHERE nid = " + c.getInt(0);

                    DataBaseHelper mDb = new DataBaseHelper(MyService.this);
                    SQLiteDatabase m = mDb.getWritableDatabase();
                    Log.e("pushcheck", pushChecked + "");
                    m.execSQL(pushChecked);

                } catch (Exception e) {

                }
            } else {
                Log.e("Intent is Null", "NULLL");
            }
        }


    }

}