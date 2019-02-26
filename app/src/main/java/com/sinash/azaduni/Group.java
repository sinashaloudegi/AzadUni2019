package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.content.Intent;
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

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;


public class Group extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    String cid;
    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        cid = getIntent().getExtras().getString("cid");


        /////////Reading From DataBase


        final TestAdapter mDbHelper = new TestAdapter(Group.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(Group.this);
        SQLiteDatabase m = mDb.getReadableDatabase();

        Cursor daneshkadeCursur = mDbHelper.getTestData("*", "tbl_groups", "college_id", cid);
        int cursurCount = daneshkadeCursur.getCount();
        String[] groupString = new String[cursurCount];
        String[] groupId = new String[cursurCount];

        byte[][] groupImage = new byte[cursurCount][];
        Cursor daneshkadeCursur2 = mDbHelper.getTestData("*", "tbl_colleges", "cid", cid);
        daneshkadeCursur.moveToFirst();
        for (int i = 0; i < cursurCount; i++) {
            groupString[i] = daneshkadeCursur.getString(2);
            groupId[i] = daneshkadeCursur.getString(0);
            groupImage[i] = daneshkadeCursur2.getBlob(4);
            daneshkadeCursur.moveToNext();
        }

        ///////////// list
        final ListView list = (ListView) findViewById(R.id.listViewGroup);
        CustomListDaneshkade adapter = new CustomListDaneshkade(this, groupString, groupId, groupImage);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Group.this, Groups.class);
                final String getPosition = (String) list.getItemAtPosition(position);
                intent.putExtra("cid", cid);
                intent.putExtra("gid", getPosition);
                startActivity(intent);


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
