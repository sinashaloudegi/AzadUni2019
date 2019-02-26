package com.sinash.azaduni;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Update extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Sinash
        AsyncGetTbl_Change asyncGetTbl_change = new AsyncGetTbl_Change();
        asyncGetTbl_change.execute();


    }


    ////Async For Get The Data From Server (tbl_changes Filling)
    private class AsyncGetTbl_Change extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(Update.this);


        public int getMax() {

            final TestAdapter mDbHelper = new TestAdapter(Update.this);
            mDbHelper.createDatabase();
            mDbHelper.open();
            Cursor c = mDbHelper.getTestData("SELECT * FROM tbl_changes WHERE flag=0");
            Cursor c2 = mDbHelper.getTestData("SELECT * FROM tbl_changes ");
            //if (c2.getCount() == 0) {
            //    return 1;
            //   } else {
            System.out.println("3 " + c2.getCount());
            return c2.getCount();
            // }
        }

        public int getMax2() {

            final TestAdapter mDbHelper = new TestAdapter(Update.this);
            mDbHelper.createDatabase();
            mDbHelper.open();
            Cursor c = mDbHelper.getTestData("SELECT * FROM tbl_changes WHERE flag=0");

            return c.getCount();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(Update.this);
            progressDialog.setMessage("در حال دریافت اطلاعات...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... arg0) {


            //WEBSERVICE
            GetData gd = new GetData();


            //Parse The Json


            try {
                JSONObject all = new JSONObject(gd.getData2("http://e-sna.net/iausdj2/public/api", (getMax() + 232) + ""));
                JSONArray dataJson = new JSONArray(all.getString("data"));
                for (int i = 0; i < dataJson.length(); i++) {
                    JSONObject JsonLine = dataJson.getJSONObject(i);

                    // JSONObject jb5 = null;
                    JSONObject value = null;
                    try {
                        value = JsonLine.getJSONObject("value");
                    } catch (JSONException e) {
                        Log.e("Error: ", e + "");
                        value = null;
                    }
                    JSONObject jb4 = JsonLine.getJSONObject("ver");

                    DataBaseHelper mDb = new DataBaseHelper(Update.this);
                    SQLiteDatabase m = mDb.getReadableDatabase();
                    String valueHelper = "\"value\": " + value;
                    m.execSQL("INSERT INTO tbl_changes(id,tbl,type,r_id,created_at,updated_at,data)\n" +
                            "VALUES ('" + jb4.getString("id") + "','" + jb4.getString("tbl") + "','" + jb4.getString("type") + "','" + jb4.getString("r_id") + "','" + jb4.getString("created_at") + "','" + jb4.getString("updated_at") + "','" + value + "')");


                }
                //it Must be delete
                final TestAdapter mDbHelper = new TestAdapter(Update.this);
                mDbHelper.createDatabase();
                mDbHelper.open();
                Cursor c = mDbHelper.getTestData("*", "tbl_changes");
/*
                for (int j = 0; j < c.getCount(); j++) {
                    Log.e("DATA :    ", c.getString(6));
                    c.moveToNext();
                }*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            //YES/NO getMax of TBL change
         //   int MAX_tbl_changes = getMax2();
         //   String stringBuilder="به روز رسانی انجام شود؟";
      /*      if (MAX_tbl_changes == 0) {

                stringBuilder = "برنامه شما به روز است";
            } else {
                //
                stringBuilder = "تعداد" + MAX_tbl_changes + " به روز رسانی موجود است";
            }*/
            AlertDialog.Builder builder = new AlertDialog.Builder(Update.this);
            builder.setCancelable(false);
            builder.setTitle("به روز رسانی");
         //   builder.setMessage(stringBuilder + "\n" + "آیا به روز رسانی اعمال شود؟");
           builder.setMessage( "آیا به روز رسانی اعمال شود؟");

            builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do do my action here

                    final TestAdapter mDbHelper = new TestAdapter(Update.this);
                    mDbHelper.createDatabase();
                    mDbHelper.open();

                    Cursor c = mDbHelper.getTestData("SELECT * FROM tbl_changes WHERE flag=0");
                    c.moveToFirst();

                    DataBaseHelper mDb = new DataBaseHelper(Update.this);
                    SQLiteDatabase m = mDb.getWritableDatabase();
                    String insertInto_tbl_profQUERY = "";
                    for (int i = 0; i < c.getCount(); i++) {
                        String tbl = c.getString(c.getColumnIndex("tbl"));


                        switch (c.getInt(c.getColumnIndex("type"))) {

                            //Insert
                            case 1:
                            case 2:

                                switch (tbl) {

                                    case "tbl_institute":
                                        ///////
                                        try {
                                            Log.e("yesss0", "tryakhr");

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            Log.e("yesss1", "tryakhr");

                                            String deleteQuery = "DELETE FROM tbl_pazh WHERE id= " + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss2", "tryakhr");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String id = dataObj.getString("id");
                                            String name = dataObj.getString("name");
                                            String image = dataObj.getString("image");
                                            String des = dataObj.getString("des");


                                            String insertInto_tbl_hamayeshQUERY = "INSERT INTO tbl_pazh VALUES('" + id + "','" + name + "','" + des + "','" + image + "')";
                                            Log.e("insertInto_tbl_hamayesh", insertInto_tbl_hamayeshQUERY);

                                            m.execSQL(insertInto_tbl_hamayeshQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            ImageDownloader imdl = new ImageDownloader(Update.this, image, "tbl_pazh", "image", "id = " + r_id);
                                            imdl.download();
                                        } catch (Exception e) {
                                            Log.e("catch Insert 33", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_boss":
                                        ///////
                                        try {

                                            String r_id = "1";
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE bid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try1");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String bid = "1";
                                            String Des = dataObj.getString("Des");

                                            String image = dataObj.getString("image");
                                            Log.e("image", image);

                                            String insertInto_tbl_assistantQUERY = "INSERT INTO tbl_boss VALUES('" + bid + "','" + Des + "','" + image + "')";
                                            Log.e("insertInto_tbl_boss", insertInto_tbl_assistantQUERY);

                                            m.execSQL(insertInto_tbl_assistantQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            ImageDownloader imdl = new ImageDownloader(Update.this, image, "tbl_boss", "image", "bid = " + bid);
                                            imdl.download();

                                        } catch (Exception e) {
                                            Log.e("catch boss", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_danshga":
                                        ///////
                                        try {

                                            String did = "1";
                                            String deleteQuery = "DELETE FROM tbl_danshgah";

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "trydanshgah");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String tbl_boos_id = did;
                                            String tbl_boss_des = dataObj.getString("des");


                                            String insertInto_tbl_hamayeshQUERY = "INSERT INTO tbl_danshgah VALUES('" + tbl_boos_id + "','" + tbl_boss_des + "')";
                                            Log.e("insertInto_tbl_danshgah", insertInto_tbl_hamayeshQUERY);

                                            m.execSQL(insertInto_tbl_hamayeshQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            Log.e("insertInto_tbl_danshgah", "twaw");
                                        } catch (Exception e) {
                                            Log.e("catch Insert 34", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_hamayesh":
                                        ///////
                                        try {
                                            Log.e("yesss0", "tryakhr");

                                            String hid = c.getString(c.getColumnIndex("r_id"));
                                            Log.e("yesss1", "tryakhr");

                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE hid= " + hid;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss2", "tryakhr");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String tbl_hamayesh_hid = dataObj.getString("hid");
                                            String tbl_hamayesh_des = dataObj.getString("des");
                                            String tbl_hamayesh_time = dataObj.getString("time");
                                            String tbl_hamayesh_link = dataObj.getString("link");

                                            String tbl_hamayesh_poster = dataObj.getString("poster");
                                            String tbl_hamayesh_isArchive = dataObj.getString("isArchive");
                                            String tbl_hamayesh_isMosabeghe = dataObj.getString("isMosabeghe");


                                            String insertInto_tbl_hamayeshQUERY = "INSERT INTO tbl_hamayesh VALUES('" + tbl_hamayesh_hid + "','" + tbl_hamayesh_des + "','" + tbl_hamayesh_time + "','" + tbl_hamayesh_link + "','" + tbl_hamayesh_poster + "','" + tbl_hamayesh_isArchive + "','" + tbl_hamayesh_isMosabeghe + "')";
                                            Log.e("insertInto_tbl_hamayesh", insertInto_tbl_hamayeshQUERY);

                                            m.execSQL(insertInto_tbl_hamayeshQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            ImageDownloader imdl = new ImageDownloader(Update.this, tbl_hamayesh_poster, "tbl_hamayesh", "poster", "hid = " + tbl_hamayesh_hid);
                                            imdl.download();
                                        } catch (Exception e) {
                                            Log.e("catch Insert 33", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_assistants":
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE aid=" + r_id;
                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try0");
///////////////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);


                                            Log.e("dataObj", dataObj + "");

                                            String assistanrt_aid = dataObj.getString("aid");
                                            String name = dataObj.getString("name");
                                            String des = dataObj.getString("des");
                                            String textImage = dataObj.getString("textImage");
                                            String menuImage = dataObj.getString("menuImage");
                                            String textImageUp = dataObj.getString("textImageUp");
                                            String menuImageUp = dataObj.getString("menuImageUp");
                                            String link = dataObj.getString("link");
                                            String ordering = dataObj.getString("ordering");
                                            String insertInto_tbl_assistantQUERY = "INSERT INTO tbl_assistants VALUES('" + assistanrt_aid + "','" + name + "','" + des + "','" + textImage + "','" + menuImage + "','" + textImageUp + "','" + menuImageUp + "','" + link + "','" + ordering + "')";
                                            Log.e("insertInto_tbl_assist", insertInto_tbl_assistantQUERY);

                                            m.execSQL(insertInto_tbl_assistantQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            ImageDownloader imdl = new ImageDownloader(Update.this, textImage, "tbl_assistants", "menuImage", "aid = " + assistanrt_aid);
                                            imdl.download();

                                        } catch (Exception e) {
                                            Log.e("catch InsertAsssis", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }

                                        break;
                                    case "tbl_az":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE az_id=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try1");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String az_id = dataObj.getString("az_id");
                                            String name = dataObj.getString("name");
                                            String des = dataObj.getString("link");
                                            String image = dataObj.getString("image");
                                            String insertInto_tbl_assistantQUERY = "INSERT INTO tbl_az VALUES('" + az_id + "','" + name + "','" + des + "','" + image + "')";
                                            Log.e("insertInto_tbl_assist", insertInto_tbl_assistantQUERY);

                                            m.execSQL(insertInto_tbl_assistantQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            ImageDownloader imdl = new ImageDownloader(Update.this, image, "tbl_az", "image", "az_id = " + az_id);
                                            imdl.download();

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_changes":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE aid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try2");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //      m.execSQL("update tbl_changes set [flag] = 1 where id = "+c.getString(0)+" ");

                                        }
                                        break;
                                    case "tbl_colleges":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE cid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try3");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String tbl_college_cid = dataObj.getString("cid");
                                            String tbl_college_name = dataObj.getString("name");
                                            String tbl_college_des = dataObj.getString("des");
                                            String tbl_college_textImage = dataObj.getString("textImage");
                                            String tbl_college_menuImage = dataObj.getString("menuImage");
                                            String tbl_college_menuImageUp = dataObj.getString("menuImageUp");
                                            String tbl_college_link = dataObj.getString("link");
                                            String alpha = dataObj.getString("alpha");

                                            String insertInto_tbl_collegesQUERY = "INSERT INTO tbl_colleges VALUES('" + tbl_college_cid + "','" + tbl_college_name + "','" + tbl_college_des + "','" + tbl_college_textImage + "','" + tbl_college_menuImage + "','" + tbl_college_menuImage + "','" + tbl_college_menuImageUp + "','" + tbl_college_link + "','" + alpha + "')";
                                            Log.e("insertInto_tbl_colleges", insertInto_tbl_collegesQUERY);

                                            m.execSQL(insertInto_tbl_collegesQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            ImageDownloader imdl = new ImageDownloader(Update.this, tbl_college_textImage, "tbl_colleges", "menuImage", "cid = " + tbl_college_cid);
                                            imdl.download();
                                        } catch (Exception e) {
                                            Log.e("catch Insert 22", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_curriculum":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE cuid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try4");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_files":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE fid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try5");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String fid = dataObj.getString("fid");
                                            String file = dataObj.getString("file");
                                            String type = dataObj.getString("type");
                                            String id = dataObj.getString("id");
                                            String image = dataObj.getString("image");
                                            String link = dataObj.getString("link");
                                            String imageUp = dataObj.getString("imageUp");

                                            String insertInto_tbl_filesQUERY = "INSERT INTO tbl_files VALUES('" + fid + "','" + file + "','" + type + "','" + id + "','" + image + "','" + link + "','" + imageUp + "')";
                                            Log.e("insertInto_tbl_files", insertInto_tbl_filesQUERY);

                                            m.execSQL(insertInto_tbl_filesQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                            ImageDownloader imdl = new ImageDownloader(Update.this, imageUp, "tbl_files", "menuImage", "fid = " + fid);
                                            imdl.download();

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_groups":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE gid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try6");

                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String groups_gid = dataObj.getString("gid");
                                            String groups_college_id = dataObj.getString("college_id");
                                            String groups_name = dataObj.getString("name");
                                            String groups_des = dataObj.getString("des");
                                            String groups_textImage = dataObj.getString("textImage");
                                            String groups_menuImage = dataObj.getString("menuImage");
                                            String groups_textImageUp = dataObj.getString("textImageUp");
                                            String groups_menuImageUp = dataObj.getString("menuImageUp");
                                            String insertInto_tbl_groupQUERY = "INSERT INTO tbl_groups  VALUES('" + groups_gid + "','" + groups_college_id + "','" + groups_name + "','" + groups_des + "','" + groups_textImage + "','" + groups_menuImage + "','" + groups_textImageUp + "','" + groups_menuImageUp + "')";
                                            Log.e("insertInto_tbl_group", insertInto_tbl_groupQUERY);
                                            m.execSQL(insertInto_tbl_groupQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            Log.e("catchdataObj", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_links":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE lid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try7");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String lid = dataObj.getString("lid");
                                            String type = dataObj.getString("type");
                                            String id = dataObj.getString("id");
                                            String title = dataObj.getString("title");
                                            String link = dataObj.getString("link");
                                            String image = dataObj.getString("image");
                                            String imageUp = dataObj.getString("imageUp");

                                            String insertInto_tbl_linksQUERY = "INSERT INTO tbl_links VALUES('" + lid + "','" + type + "','" + id + "','" + title + "','" + link + "','" + image + "','" + imageUp + "')";
                                            Log.e("insertInto_tbl_links", insertInto_tbl_linksQUERY);

                                            m.execSQL(insertInto_tbl_linksQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");
                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_news":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE nid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try8");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_peoples":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE pid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try9");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_phones":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE phoneid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try10");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String phoneid = dataObj.getString("phoneid");
                                            String mid = dataObj.getString("mid");
                                            String rank = dataObj.getString("rank");


                                            String insertInto_tbl_phoneQUERY = "INSERT INTO tbl_phones VALUES('" + phoneid + "','" + mid + "','" + rank + "')";
                                            Log.e("insertInto_tbl_links", insertInto_tbl_phoneQUERY);

                                            m.execSQL(insertInto_tbl_phoneQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");
                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_managers":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE mid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try11");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String mid = dataObj.getString("mid");
                                            String people_id = dataObj.getString("people_id");
                                            String type = dataObj.getString("type");
                                            String post = dataObj.getString("post");
                                            String phone = dataObj.getString("phone");
                                            String type_id = dataObj.getString("type_id");
                                            String rank = dataObj.getString("rank");

                                            String insertInto_tbl_managersQUERY = "INSERT INTO tbl_managers VALUES('" + mid + "','" + people_id + "','" + type + "','" + post + "','" + phone + "','" + type_id + "','" + rank + "')";
                                            Log.e("insertInto_tbl_manager", insertInto_tbl_managersQUERY);

                                            m.execSQL(insertInto_tbl_managersQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");
                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_prof":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE pid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try12");
                                            //////////////////////////
                                            String dataJson = c.getString(c.getColumnIndex("data"));

                                            JSONObject dataObj = new JSONObject(dataJson);

                                            String pid = dataObj.getString("pid");
                                            String type = dataObj.getString("type");
                                            String name = dataObj.getString("name");
                                            String family = dataObj.getString("family");
                                            String image = dataObj.getString("image");
                                            String phone = dataObj.getString("phone");
                                            String imageUp = dataObj.getString("imageUp");
                                            String college_id = dataObj.getString("college_id");
                                            String group_id = dataObj.getString("group_id");
                                            String MADRAK = dataObj.getString("evidence");
                                            String degree = dataObj.getString("degree");
                                            String major = dataObj.getString("major");
                                            String link = dataObj.getString("link");
                                            String resume = dataObj.getString("resume");
                                            String email = dataObj.getString("email");

                                            insertInto_tbl_profQUERY = "INSERT INTO tbl_prof VALUES('" + pid + "','" + type + "','" + name + "','" + family + "','" + null + "','" + phone + "','" + imageUp + "','" + college_id + "','" + group_id + "','" + MADRAK + "','" + degree + "','" + major + "','" + link + "','" + resume + "','" + email + "')";
                                            Log.e("insertInto_tbl_prof", insertInto_tbl_profQUERY);
                                            m.execSQL(insertInto_tbl_profQUERY);
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");
                                            Log.e("insertInto_tbl_prof2", insertInto_tbl_profQUERY);

                                            Log.e("insertInto_tbl_prof2.5", image);
                                            ImageDownloader imdl = new ImageDownloader(Update.this, image, "tbl_prof", "image", "pid = " + pid);
                                            Log.e("insertInto_tbl_prof3", insertInto_tbl_profQUERY);

                                            imdl.download();
                                            Log.e("insertInto_tbl_prof4", insertInto_tbl_profQUERY);

                                        } catch (Exception e) {
                                            Log.e("catch Insert ostad", insertInto_tbl_profQUERY);
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_professors":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE people_id=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss", "try13");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    default:


                                }

                                break;


                            //Delete
                            case 3:


                                switch (tbl) {

                                    case "tbl_institute":
                                        try {

                                            String hid = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM tbl_pazh WHERE id=" + hid;
                                            m.execSQL(deleteQuery);

                                            Log.e("yesss33", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_hamayesh":
                                        try {

                                            String hid = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE hid=" + hid;
                                            m.execSQL(deleteQuery);

                                            Log.e("yesss33", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_assistants":
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE aid=" + r_id;
                                            m.execSQL(deleteQuery);

                                            Log.e("yesss0", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_az":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE az_id=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss1", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_changes":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE aid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss2", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_colleges":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE cid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss3", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            Log.e("catch Insert college", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_curriculum":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE cuid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss4", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_files":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE fid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss5", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            Log.e("catch Insert5", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_groups":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE gid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss6", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_links":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE lid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss7", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_news":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE nid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss8", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_peoples":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE pid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss9", "try");

                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");


                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //    m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_phones":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE phoneid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss10", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");


                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_managers":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE mid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss11", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");


                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //   m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_prof":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE pid=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss12", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");


                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_professors":
                                        ///////
                                        try {

                                            String r_id = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE people_id=" + r_id;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss13", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");


                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_boss":
                                        ///////
                                        try {

                                            String bid = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE bid= " + 1;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss13", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");


                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    case "tbl_danshgah":
                                        ///////
                                        try {

                                            String did = c.getString(c.getColumnIndex("r_id"));
                                            String deleteQuery = "DELETE FROM " + tbl + " WHERE did=" + did;

                                            m.execSQL(deleteQuery);
                                            Log.e("yesss14", "try");
                                            m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");


                                        } catch (Exception e) {
                                            //   Log.e("catch Insert", "ssssssss");
                                            //  m.execSQL("update tbl_changes set [flag] = 1 where id = " + c.getString(0) + " ");

                                        }
                                        break;
                                    default:


                                }


                                break;

                        }
                        c.moveToNext();

                        progressDialog.setMessage(i + "");
                    }

                    dialog.dismiss();
                    Toast.makeText(Update.this, "تغییرات بعد از اجرای دوباره برنامه قابل مشاهده است", Toast.LENGTH_LONG).show();
                    finish();
                }

            });

            builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // I do not need any action here you might
                    dialog.dismiss();
                    finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }

        @Override
        protected void onCancelled() {

            super.onCancelled();
            progressDialog.dismiss();

        }

    }


}
