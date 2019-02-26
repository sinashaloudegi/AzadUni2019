package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;


public class Asatid extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    public int error = 0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_asatid);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        ////////
        try {
            String cid = getIntent().getExtras().getString("cid");
            final TestAdapter mDbHelper = new TestAdapter(Asatid.this);
            mDbHelper.createDatabase();
            mDbHelper.open();

            DataBaseHelper mDb = new DataBaseHelper(Asatid.this);
            SQLiteDatabase m = mDb.getReadableDatabase();

            String selectCIDorPID = getIntent().getExtras().getString("select");
            Cursor asatidCursur = null;
            switch (selectCIDorPID) {

                case "D":
                    asatidCursur = mDbHelper.getTestData("*", "tbl_prof", "college_id", cid);


                    break;
                case "G":
                    asatidCursur = mDbHelper.getTestData("*", "tbl_prof", "group_id", cid);

                    break;
            }

            int getCurserCount = asatidCursur.getCount();

            String[] name = new String[getCurserCount],

                    reshte = new String[getCurserCount],
                    email = new String[getCurserCount],
                    link = new String[getCurserCount],
                    madrak = new String[getCurserCount],
                    daraje = new String[getCurserCount],
                    rezoome = new String[getCurserCount];
            byte[][] image = new byte[getCurserCount][];


            ///////////// list
            ListView list = (ListView) findViewById(R.id.listViewAsatid);
            asatidCursur.moveToFirst();

            for (int i = 0; i < getCurserCount; i++) {
                int pid = asatidCursur.getInt(0);


                name[i] = asatidCursur.getString(2) + " " + asatidCursur.getString(3);

                image[i] = asatidCursur.getBlob(4);

                if (asatidCursur.isNull(11)) {

                    reshte[i] = "null";
                } else
                    reshte[i] = asatidCursur.getString(11);

                link[i] = asatidCursur.getString(12);


                madrak[i] = asatidCursur.getString(9);

                daraje[i] = asatidCursur.getString(10);

                rezoome[i] = asatidCursur.getString(13);
                /////this the file col. in database

                email[i] = asatidCursur.getString(14);
                error = pid;
                asatidCursur.moveToNext();
            }
            CustomListAsatid adapter = new CustomListAsatid(this, name, reshte, email, link, madrak, daraje, rezoome, image);
            SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
            animationAdapter.setAbsListView(list);
            list.setAdapter(animationAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                }
            });
        } catch (Exception e) {
            Toast.makeText(this, error + "   " + e + "  ", Toast.LENGTH_LONG).show();
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
