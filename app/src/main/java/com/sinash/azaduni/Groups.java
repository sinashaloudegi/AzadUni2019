package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class Groups extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    String gid, cid;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        ////get pos
        gid = getIntent().getExtras().getString("gid");
        cid = getIntent().getExtras().getString("cid");
        final TestAdapter mDbHelper = new TestAdapter(Groups.this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(Groups.this);
        SQLiteDatabase m = mDb.getReadableDatabase();

        Cursor daneshkadeCursur = mDbHelper.getTestData("*", "tbl_groups", "gid", gid, "college_id", cid);
        Cursor daneshkadeCursur2 = mDbHelper.getTestData("*", "tbl_colleges", "cid", cid);
        int cursurCount = daneshkadeCursur.getCount();
        String name = daneshkadeCursur.getString(2);

        String des = " ";
        if (!daneshkadeCursur.isNull(3)) {
            des = daneshkadeCursur.getString(3);
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageViewgroups);
        try {
            byte[] image = daneshkadeCursur2.getBlob(4);
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
        TextView res = (TextView) findViewById(R.id.textViewGroup);
        res.setText(name);
        JustifiedTextView title = (JustifiedTextView) findViewById(R.id.textViewdesGroup);
        //    WebView web= (WebView) findViewById(R.id.webViewdag);
        //  web.loadData(des, "text/html; charset=UTF-8", null);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        title.setLineSpacing(10);
        title.setAlignment(Paint.Align.RIGHT);

        title.setPadding(50, 50, 50, 50);
        title.setText(des);
        Typeface font = Typeface.createFromAsset(getAssets(), "BNazanin.ttf");

        title.setTypeFace(font);
        ///set animation to views
        HorizontalScrollView hscroll = (HorizontalScrollView) findViewById(R.id.horizontalScrol);
        ScrollView vscroll = (ScrollView) findViewById(R.id.scrol);
        Animation animationH = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animh);
        Animation animationV = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animv);
        ImageButton b0 = (ImageButton) findViewById(R.id.btnOstad);

        ImageButton b3 = (ImageButton) findViewById(R.id.btnModiriat);
        ImageButton b4 = (ImageButton) findViewById(R.id.btnAkhbar);
        ImageButton b5 = (ImageButton) findViewById(R.id.btnForm);

        vscroll.startAnimation(animationV);
        //hscroll.startAnimation(animationH);
        b0.startAnimation(animationH);

        b3.startAnimation(animationH);
        b4.startAnimation(animationH);
        b5.startAnimation(animationH);

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

    public void OnclickgAkhbar2(View view) {

        Intent goToAkhbar = new Intent(Groups.this, NewsList.class);
        startActivity(goToAkhbar);
    }

    public void OnclickOstad2(View view) {

        Intent goToOstad = new Intent(Groups.this, Asatid.class);
        goToOstad.putExtra("cid", gid);
        goToOstad.putExtra("select", "G");
        startActivity(goToOstad);
    }

    public void OnclickgModiriat2(View view) {

        Intent goToModiriat = new Intent(Groups.this, Modiriat.class);
        goToModiriat.putExtra("type", "3");
        goToModiriat.putExtra("type_id", gid);
        startActivity(goToModiriat);
    }

    public void OnclickFile2(View view) {
        new AlertDialog.Builder(Groups.this)
                .setTitle("ناموجود")
                .setMessage("اطلاعات درخواستی فعلا موجود نمی باشد !")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).create().show();

       /* Intent goToFile = new Intent(DaneshkadeHa.this,Link.class);
        goToFile.putExtra("type", "2");
        goToFile.putExtra("id", getPos);
        goToFile.putExtra("ForL","F");

        startActivity(goToFile);*/
    }

}
