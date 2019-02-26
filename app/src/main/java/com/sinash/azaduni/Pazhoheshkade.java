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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;


public class Pazhoheshkade extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pazhoheshkade);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /////////Reading From DataBase


        final TestAdapter mDbHelper = new TestAdapter(Pazhoheshkade.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(Pazhoheshkade.this);
        SQLiteDatabase m = mDb.getReadableDatabase();

        Cursor daneshkadeCursur = mDbHelper.getTestData("*", "tbl_az");
        int cursurCount = daneshkadeCursur.getCount();
        String[] daneshkadeString = new String[cursurCount];
        String[] daneshkadeId = new String[cursurCount];
        byte[][] daneshkadeImage = new byte[cursurCount][];

        daneshkadeCursur.moveToFirst();
        for (int i = 0; i < cursurCount; i++) {
            daneshkadeString[i] = daneshkadeCursur.getString(1);
            daneshkadeId[i] = daneshkadeCursur.getString(2);
            daneshkadeImage[i] = daneshkadeCursur.getBlob(3);
            daneshkadeCursur.moveToNext();
        }

        ///////////// List
        final ListView list = (ListView) findViewById(R.id.listViewPazh);
        CustomListDaneshkade adapter = new CustomListDaneshkade(this, daneshkadeString, daneshkadeId, daneshkadeImage);
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
