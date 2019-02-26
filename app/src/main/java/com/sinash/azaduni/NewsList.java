
package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;


public class NewsList extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /////////////////////////////////////////////////////////
        final ListView lv = (ListView) findViewById(R.id.listView);
        String NewsListString[] = {"همه اخبار",
                "اخبار معاونت آموزشی",
                "اخبار معاونت پژوهشی",
                "اخبار معاونت اداری مالی",
                "اخبار معاونت دانشجویی و فرهنگی"};
        String NewsListString_id[] = {"0",
                "1",
                "2",
                "3",
                "4"};


        final TestAdapter mDbHelper = new TestAdapter(NewsList.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(NewsList.this);
        SQLiteDatabase m = mDb.getReadableDatabase();

        Cursor allnewsCursur = mDbHelper.getTestData("*", "tbl_news", "seen", "0");
        int cursurCount = allnewsCursur.getCount();
        Cursor allnewsCursur1 = mDbHelper.getTestData("*", "tbl_news", "type", "1", "seen", "0");
        int cursurCount1 = allnewsCursur1.getCount();
        Cursor allnewsCursur2 = mDbHelper.getTestData("*", "tbl_news", "type", "2", "seen", "0");
        int cursurCount2 = allnewsCursur2.getCount();
        Cursor allnewsCursur3 = mDbHelper.getTestData("*", "tbl_news", "type", "3", "seen", "0");
        int cursurCount3 = allnewsCursur3.getCount();
        Cursor allnewsCursur4 = mDbHelper.getTestData("*", "tbl_news", "type", "4", "seen", "0");
        int cursurCount4 = allnewsCursur4.getCount();

        String count[] = {cursurCount + "",
                cursurCount1 + "",
                cursurCount2 + "",
                cursurCount3 + "",
                cursurCount4 + ""};

        CustomListkhabar adapter = new CustomListkhabar(this, NewsListString, NewsListString_id, count);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(lv);
        lv.setAdapter(animationAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getPosition = (String) lv.getItemAtPosition(position);


                Intent goToGroup = new Intent(NewsList.this, Akhbar.class);
                goToGroup.putExtra("type", getPosition);

                startActivity(goToGroup);

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        Log.d("The onResume()", "The onResume() event");
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


    public void getNewsServer() {
        final TestAdapter mDbHelper = new TestAdapter(NewsList.this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        for (int j = 0; j < 10; j++) {
            DataBaseHelper mDb = new DataBaseHelper(NewsList.this);
            SQLiteDatabase m = mDb.getReadableDatabase();

            int newsMax = maxNews("tbl_news");
            System.out.println("max `   " + newsMax);
            GetData gd = new GetData();

            List<News> allNewse = new ArrayList<>();

            String maxServerString=gd.getData1("http://e-sna.net/iausdj2/json/max_news.php");
            int maxServer=0;
            Log.e("maxServerString", maxServerString);
            if(maxServerString.length() > 1){
                maxServer = Integer.parseInt(maxServerString.substring(1));
            }
            Log.e("maxServer", maxServer + "");
            if (maxServer > newsMax) {

                String json = gd.getData("http://e-sna.net/iausdj2/json/news_json.php", newsMax);
                Log.e("json", json + "");

                NewsParser np = new NewsParser(this);
                allNewse = np.newsParse(json);
                Sql sql = new Sql(this);
                for (int i = 0; i < allNewse.size(); i++) {

                    sql.insertNews(allNewse.get(i));
                }

            }
        }
    }


    private class Async extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(NewsList.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(NewsList.this);
            progressDialog.setMessage("در حال دریافت اخبار...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //do code here
            getNewsServer();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            finish();
            startActivity(getIntent());
        }

        @Override
        protected void onCancelled() {

            super.onCancelled();
            progressDialog.dismiss();


        }

    }


    public void updateClick(View view) {
        if (isInternet()) {
            try {
                Async start = new Async();
                start.execute();
            } catch (Exception e) {

                Toast.makeText(NewsList.this, "لطفا به اینترنت متصل شوید!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(NewsList.this, "لطفا به اینترنت متصل شوید!", Toast.LENGTH_LONG).show();
        }

    }

    public int maxNews(String tableName) {
        int max = -1;
        SQLiteDatabase database;
        database = this.openOrCreateDatabase("azad.db",
                Context.MODE_PRIVATE, null);

        Cursor selectCursor = database.rawQuery("SELECT MAX(nid) FROM "
                + tableName, null);

        if (selectCursor == null) {

        } else {
            if (selectCursor.moveToFirst()) {
                do {

                    max = selectCursor.getInt(0);

                } while (selectCursor.moveToNext());
            }
        }

        return max;
    }

    private boolean isInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}





