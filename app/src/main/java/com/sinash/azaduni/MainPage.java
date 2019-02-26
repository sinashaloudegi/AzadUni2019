package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;


public class MainPage extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    static Point size;

    static float density;
    JustifiedTextView about_text_collage;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        DataBaseHelper mDb = new DataBaseHelper(MainPage.this);
        try {
            mDb.createDataBase();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        ////////////////////////////////////////
        final TestAdapter mDbHelper = new TestAdapter(MainPage.this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor daneshkadeCursur = mDbHelper.getTestData("*", "tbl_danshgah");
        Log.e("ttt", daneshkadeCursur.getCount() + "");
        String txthtml = "دانشگاه آزاد اسلامی واحد سنندج در ۲۳ شهریور ۱۳۶۲ در شهر سنندج مرکز استان کردستان ایران بنیاد شد. و فعالیت آموزشی خود را با ۲۲ نفر دانشجو در رشته کاردانی کشاورزی آغاز نمود.\n" +
                "این واحد دانشگاهی هم اکنون با ۷ دانشکده مصوب: فنی و مهندسی، دامپزشکی، کشاورزی و منابع طبیعی، علوم انسانی، پرستاری و مامایی، علوم پایه، روانشناسی و علوم تربیتی از بزرگترین دانشگاه\u200Cهای منطقه با رتبه بسیار بزرگ سطح الف به شمار می\u200Cآید. در آخرین روزهای ریاست آقای دکتر جاسبی به صورت مشروط به واحد چامع ارتقا یافت اما بعد از شروع دوره ریاست فعلی دانشگاه، آقای دکتر دانشجو، ارتقای آن به حالت تعلیق در آمده است.\n" +
                "\n" +
                "این واحد در حال حاضر دارای ۲۸۹ عضو هیأت علمی و ۱۲۰ رشته تحصیلی با بیش از ۱۱۰۰۰ دانشجو در مقاطع کاردانی، کارشناسی و کارشناسی ارشد است.";
        try {
            txthtml = daneshkadeCursur.getString(1);
        } catch (Exception e) {
        }
        for (int i = 0; i < daneshkadeCursur.getCount(); i++) {

            Log.e("ttt2", daneshkadeCursur.getString(0) + "");
        }
        /////////////////////////////////////////
        JustifiedTextView mainMatn = (JustifiedTextView) findViewById(R.id.textView);
        mainMatn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mainMatn.setLineSpacing(10);
        mainMatn.setAlignment(Paint.Align.RIGHT);

        mainMatn.setPadding(50, 50, 50, 50);

        mainMatn.setText(txthtml);

        Typeface font = Typeface.createFromAsset(getAssets(), "BZar.ttf");
        //  mainMatn.setText(txt);
        mainMatn.setTypeFace(font);

//////////////////////////////////////

      /*  Display display = getWindowManager().getDefaultDisplay();
         size = new Point();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        display.getSize(size);

        TextJustification.justify(mainMatn, size.x);*/


        ////////////////set animation to main text (Linear Layout)
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.matnLayout);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
        mainLayout.startAnimation(animation);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        Animation animationFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation2);
        mainMatn.startAnimation(animationFade);

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

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        alertDialog.setTitle("خروج از برنامه");
        alertDialog.setMessage("آ?ا ما?ل به خروج هست?د؟");
        alertDialog.setPositiveButton("بل?",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        finish();
                    }
                });
        alertDialog.setNegativeButton("خ?ر",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


}
