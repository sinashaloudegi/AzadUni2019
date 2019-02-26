package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

//import android.util.Log;


public class DaneshkadeHa extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    String getPos;

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daneshkade_ha);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
////get POS
        getPos = getIntent().getExtras().getString("pos");
        final TestAdapter mDbHelper = new TestAdapter(DaneshkadeHa.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(DaneshkadeHa.this);
        SQLiteDatabase m = mDb.getReadableDatabase();

        Cursor daneshkadeCursur = mDbHelper.getTestData("*", "tbl_colleges", "cid", getPos);
        int cursurCount = daneshkadeCursur.getCount();

        String name = daneshkadeCursur.getString(1);
        String des = daneshkadeCursur.getString(2);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewdaneshkade);
        try {
            byte[] image = daneshkadeCursur.getBlob(4);
            InputStream inputStream = new ByteArrayInputStream(image);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {

                imageView.setImageResource(R.drawable.photodaneshkade);

            } else {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {

            imageView.setImageResource(R.drawable.photodaneshkade);
        }
        TextView res = (TextView) findViewById(R.id.textViewdaneshkade);
        res.setText(name);
        JustifiedTextView title = (JustifiedTextView) findViewById(R.id.textViewdes);
        // WebView web= (WebView) findViewById(R.id.webViewda);
        //   web.loadDataWithBaseURL("file:///android_asset/",des, "text/html","UTF-8", null);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        title.setLineSpacing(12);
        title.setAlignment(Paint.Align.RIGHT);

        title.setPadding(50, 50, 50, 50);
        title.setText(des);
        Typeface font = Typeface.createFromAsset(getAssets(), "BNazanin.ttf");

        title.setTypeFace(font);
        ///set animation to views
        HorizontalScrollView hscroll = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        ScrollView vscroll = (ScrollView) findViewById(R.id.scrollView2);
        Animation animationH = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animh);
        Animation animationV = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animv);
        ImageButton b0 = (ImageButton) findViewById(R.id.imageButton);
        ImageButton b2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton b3 = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton b4 = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton b5 = (ImageButton) findViewById(R.id.imageButton5);
        ImageButton b6 = (ImageButton) findViewById(R.id.imageButton6);
        ImageButton b7 = (ImageButton) findViewById(R.id.imageButton7);

        vscroll.startAnimation(animationV);
        //hscroll.startAnimation(animationH);
        b0.startAnimation(animationH);
        b2.startAnimation(animationH);
        b3.startAnimation(animationH);
        b4.startAnimation(animationH);
        b5.startAnimation(animationH);
        b7.startAnimation(animationH);
        b6.startAnimation(animationH);


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

    public void OnclickOstad(View view) {

        Intent goToOstad = new Intent(DaneshkadeHa.this, Asatid.class);
        //Log.e("cid=", getPos);
        goToOstad.putExtra("cid", getPos);
        goToOstad.putExtra("select", "D");
        startActivity(goToOstad);
    }

    public void OnclickGroup(View view) {

        Intent goToGroup = new Intent(DaneshkadeHa.this, Group.class);
        goToGroup.putExtra("cid", getPos);

        startActivity(goToGroup);
    }

    public void OnclickgAkhbar(View view) {

        Intent goToAkhbar = new Intent(DaneshkadeHa.this, NewsList.class);
        startActivity(goToAkhbar);
    }

    public void OnclickgModiriat(View view) {

        Intent goToModiriat = new Intent(DaneshkadeHa.this, Modiriat.class);
        goToModiriat.putExtra("type", "2");
        goToModiriat.putExtra("type_id", getPos);

        startActivity(goToModiriat);
    }

    public void OnclickFile(View view) {
        new AlertDialog.Builder(DaneshkadeHa.this);


        Intent goToFile = new Intent(DaneshkadeHa.this, Link.class);
        goToFile.putExtra("type", "1");
        goToFile.putExtra("id", getPos);
        goToFile.putExtra("ForL", "F");

        startActivity(goToFile);
    }


    public void OnclickSite(View view) {
        final TestAdapter mDbHelper = new TestAdapter(DaneshkadeHa.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(DaneshkadeHa.this);
        SQLiteDatabase m = mDb.getReadableDatabase();
        Cursor webCursur = mDbHelper.getTestData("*", "tbl_colleges", "cid", getPos);
        Intent myIntent = null;
        try {
            if (!webCursur.isNull(7)) {
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webCursur.getString(7)));
                startActivity(myIntent);
            } else {
                Toast.makeText(this, "سایت مربوطه موجود نمی باشد", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
    }

    public void Onclickdlink2(View view) {
        Intent goToLink = new Intent(DaneshkadeHa.this, Link.class);
        goToLink.putExtra("ForL", "L");
        goToLink.putExtra("type", "1");
        goToLink.putExtra("DoMid", getPos);
        startActivity(goToLink);

    }
}
