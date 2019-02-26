package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;


public class Joziyatham extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joziyatham);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /////////////////////////////////////
        Intent is = getIntent();
        String isStrng = is.getExtras().getString("is");
        final TestAdapter mDbHelper = new TestAdapter(Joziyatham.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(Joziyatham.this);
        SQLiteDatabase m = mDb.getReadableDatabase();

        Cursor joziatHamCur = null;
        switch (isStrng) {
            case "ham1":
                joziatHamCur = mDbHelper.getTestData("*", "tbl_hamayesh", "isMosabeghe", "0", "isArchive", "0");


                break;
            case "ham2":
                joziatHamCur = mDbHelper.getTestData("*", "tbl_hamayesh", "isMosabeghe", "0", "isArchive", "1");

                break;
            case "mos1":
                joziatHamCur = mDbHelper.getTestData("*", "tbl_hamayesh", "isMosabeghe", "1", "isArchive", "0");

                break;
            case "mos2":
                joziatHamCur = mDbHelper.getTestData("*", "tbl_hamayesh", "isMosabeghe", "1", "isArchive", "1");

                break;
            default:
                Log.e("JOZYATHAM", "SwtichError");

        }
        int getCurserCount = joziatHamCur.getCount();
        String[] titleham = new String[getCurserCount];
        String[] timeham = new String[getCurserCount];
        String[] linkham = new String[getCurserCount];
        byte[][] image = new byte[getCurserCount][];
        joziatHamCur.moveToFirst();
        for (int i = 0; i < getCurserCount; i++) {

            titleham[i] = joziatHamCur.getString(1);
            timeham[i] = joziatHamCur.getString(2);
            linkham[i] = joziatHamCur.getString(3);
            image[i] = joziatHamCur.getBlob(4);
            joziatHamCur.moveToNext();
        }

        ///////////// list
        final ListView list = (ListView) findViewById(R.id.listHam);

        CustomListHam adapter = new CustomListHam(this, titleham, timeham, linkham, image);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String getPosition = (String) list.getItemAtPosition(position);


                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getPosition));
                startActivity(myIntent);


            }
        });
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
