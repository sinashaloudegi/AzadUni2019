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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;


public class Akhbar extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_akhbar);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        String type = getIntent().getExtras().getString("type");
        /////////Reading From DataBase


        final TestAdapter mDbHelper = new TestAdapter(Akhbar.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(Akhbar.this);
        SQLiteDatabase m = mDb.getReadableDatabase();
        Cursor AkhbarCursur = null;
        if (type.equals("0")) {
            AkhbarCursur = mDbHelper.getTestData("*", "tbl_news");
        } else {

            AkhbarCursur = mDbHelper.getTestData("*", "tbl_news", "type", type);
        }


        int cursurCount = AkhbarCursur.getCount();
        if (cursurCount == 0) {

            Toast.makeText(Akhbar.this, "خبری در این بخش وجود ندارد", Toast.LENGTH_LONG).show();

        }
        String[] AkhbarString = new String[cursurCount];
        String[] AkhbarId = new String[cursurCount];
        String[] AkhbarTag = new String[cursurCount];
        String[] AkhbarDate = new String[cursurCount];
        String[] seen = new String[cursurCount];
        byte[][] AkhbarImage = new byte[cursurCount][];

        AkhbarCursur.moveToFirst();
        for (int i = 0; i < cursurCount; i++) {
            AkhbarString[i] = AkhbarCursur.getString(2);
            AkhbarId[i] = AkhbarCursur.getString(0);
            String tag = AkhbarCursur.getString(1);
            switch (tag) {

                case "1":
                    AkhbarTag[i] = "معاونت آموزشی";
                    break;
                case "2":
                    AkhbarTag[i] = "معاونت پژوهشی";
                    break;
                case "3":
                    AkhbarTag[i] = "معاونت اداری و مالی";
                    break;
                case "4":
                    AkhbarTag[i] = "معاونت دانشجویی و فرهنگی";
                    break;
                default:
                    AkhbarTag[i] = "همه اخبار";
                    break;
            }

            AkhbarDate[i] = AkhbarCursur.getString(7);
            AkhbarImage[i] = AkhbarCursur.getBlob(4);
            seen[i] = AkhbarCursur.getString(9);
            AkhbarCursur.moveToNext();
        }

        ///////////// List
        final ListView list = (ListView) findViewById(R.id.listViewAkhbar);
        CustomListAkhbar adapter = new CustomListAkhbar(this, AkhbarString, AkhbarId, AkhbarTag, AkhbarDate, AkhbarImage, seen);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getPosition = (String) list.getItemAtPosition(position);
                Intent intent = new Intent(Akhbar.this, JoziyateKhabar.class);
                intent.putExtra("idkhabar", getPosition);
                intent.putExtra("tbl", "tbl_news");
                startActivity(intent);

            }
        });

    }


    /**
     * Called when the activity has become visible.
     */
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


}
