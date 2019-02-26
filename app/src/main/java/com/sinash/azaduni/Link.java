package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import java.io.File;


public class Link extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    int shart = 0;

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /////////Reading From DataBase


        final TestAdapter mDbHelper = new TestAdapter(Link.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

            DataBaseHelper mDb = new DataBaseHelper(Link.this);
        SQLiteDatabase m = mDb.getReadableDatabase();


        final String select = getIntent().getExtras().getString("ForL");
        Cursor LinkCursur = null;
        String[] LinkString = null;
        String[] LinkId = null;
        String[] LinkType = null;
        String type = null;
        String id = null;

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        switch (select) {


            case "F":
                type = getIntent().getExtras().getString("type");
                actionBar.setTitle("فایل ها و آئین نامه ها");
                id = getIntent().getExtras().getString("id");

                LinkCursur = mDbHelper.getTestData("*", "tbl_files", "type", type, "id", id);
                int cursurCount = LinkCursur.getCount();
                LinkString = new String[cursurCount];
                LinkId = new String[cursurCount];


                for (int i = 0; i < cursurCount; i++) {

                    LinkString[i] = LinkCursur.getString(1);
                    LinkId[i] = LinkCursur.getString(0);

                    Log.w("String = ", LinkString[i]);
                    Log.w("ID=", LinkId[i]);
                    LinkCursur.moveToNext();
                }

                break;

            case "L":
                type = getIntent().getExtras().getString("type");
                Log.e("typeeeeeeeeeee",type+"");
                if(type==null) {
                    LinkCursur = mDbHelper.getTestData("*", "tbl_links");
                }else{
                    //daneshkade or Moavenat id; based on type: 0 for moavenat. 1 for daneshkade
                    String DoMid= getIntent().getExtras().getString("DoMid");
                    LinkCursur = mDbHelper.getTestData("*", "tbl_links where type = "+type+" and id = "+DoMid);

                }
                int cursurCount2 = LinkCursur.getCount();
                LinkString = new String[cursurCount2];
                LinkId = new String[cursurCount2];
                LinkType = new String[cursurCount2];

                for (int i = 0; i < cursurCount2; i++) {

                    LinkString[i] = LinkCursur.getString(3);
                    LinkId[i] = LinkCursur.getString(4);
                    LinkType[i] = LinkCursur.getString(1);
                    LinkCursur.moveToNext();
                }
                break;
        }


        ///////////// list
        final ListView list = (ListView) findViewById(R.id.listViewLink);
        CustomListLink adapter = new CustomListLink(this, LinkString, LinkId);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);

        final String finalType = type;
        final String finalid = id;
        final String finalType1 = type;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String getPosition = (String) list.getItemAtPosition(position);
                switch (select) {


                    case "F":

                        Cursor fileCursur = mDbHelper.getTestData("link", "tbl_files", "fid", getPosition);
                        String url = fileCursur.getString(0);
                        String name = finalType1 + id + getPosition + "";
                        File file = new File(Environment.getExternalStorageDirectory(), "AzadUni");

                        Log.w("url= ", url);
                        Log.w("name = ", name);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                      /*  String SDCardRoot = Environment.getExternalStorageDirectory().getPath() + "/azadUni/" ;
                        //create a new file, to save the downloaded file
                        File file = new File(SDCardRoot);*/
                        if (shart == 0) {
                            Log.w("dir = ", Environment.getExternalStorageDirectory().toString());
                            Log.w("filename = ", "AzadUni/" + name + ".pdf");
                            downloadAndOpenPdf(url, new File(Environment.getExternalStorageDirectory(), "AzadUni/" + name + ".pdf"));
                        }
                        break;

                    case "L":
                        try {


                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getPosition));
                            startActivity(myIntent);
                        } catch (Exception e) {

                            Toast.makeText(Link.this, "لینک مورد نظر موجود نیست", Toast.LENGTH_LONG).show();
                        }
                }

            }
        });
    }

    public void downloadAndOpenPdf(String url, final File file) {
        if (!file.isFile()) {
            shart = 1;
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
            req.setDestinationUri(Uri.fromFile(file));
            req.setTitle("در حال دانلود آئین نامه...");

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    shart = 0;
                    unregisterReceiver(this);
                    if (file.exists()) {
                        openPdfDocument(file);
                    }
                }
            };
            registerReceiver(receiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            dm.enqueue(req);

            Toast.makeText(this, "در حال دانلود فایل...", Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, "لطفا صبر کنید ...", Toast.LENGTH_LONG).show();
        } else {
            openPdfDocument(file);
        }
    }

    public boolean openPdfDocument(File file) {
        shart = 0;
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(target);
            return true;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No PDF reader found", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
            return rootView;
        }


    }


}
