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
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;


public class Phone extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
/////////////
        final TestAdapter mDbHelper = new TestAdapter(Phone.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(Phone.this);
        SQLiteDatabase m = mDb.getReadableDatabase();
        //Cursor modiriatCursur = mDbHelper.getTestData("*", "tbl_managers", "type", "4" + " ORDER BY rank ASC");
        Cursor modiriatCursur = mDbHelper.getTestData("select m.* from tbl_phones ph, tbl_managers m where m.mid=ph.mid order by ph.rank");
        int getCurserCount = modiriatCursur.getCount();
        String[] name = new String[getCurserCount];
        String[] semat = new String[getCurserCount];
        String[] tell = new String[getCurserCount];
        byte[][] image = new byte[getCurserCount][];
        final String[] link = new String[getCurserCount];
        modiriatCursur.moveToFirst();
        for (int i = 0; i < getCurserCount; i++) {
            int pid = modiriatCursur.getInt(1);
            Cursor Cursur = mDbHelper.getTestData("*", "tbl_prof", "pid", pid + "");
            name[i] = Cursur.getString(2) + " " + Cursur.getString(3);
            semat[i] = modiriatCursur.getString(3);
            tell[i] = modiriatCursur.getString(4);
            image[i] = Cursur.getBlob(4);
            link[i] = Cursur.getString(12);
            modiriatCursur.moveToNext();
        }

        ///////////// list
        final ListView list = (ListView) findViewById(R.id.listViewOstad);

        CustomListPhone adapter = new CustomListPhone(this, name, semat, tell, image, link);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final String getPosition = (String) list.getItemAtPosition(position);
                try {

                    System.out.println("Test");
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getPosition));
                    startActivity(myIntent);
                } catch (Exception e) {

                    Toast.makeText(Phone.this, "???? ???? ??? ????? ????", Toast.LENGTH_LONG).show();
                }
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
