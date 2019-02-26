package com.sinash.azaduni;


import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class Riasat extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riasat);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        String txt = "مشخصات رييس دانشگاه آزاد اسلامي واحد سنندج\n" +
                "\n" +
                "     نام و نام خانوادگي: محمد قربان كياني\n" +
                "\n" +
                "    عضو هيات علمي واحد سنندج\n" +
                "\n" +
                "    سال تولد: 1342\n" +
                "\n" +
                "      محل صدور: قروه\n" +
                "\n" +
                "    پست الكترونيك:\n" +
                " mohammad.kiany@yahoo.com\n" +
                "\n" +
                "      تاريخ انتصاب: 8/8/1391\n" +
                "\n" +
                "     مدرك تحصیلی: دكتري تخصصي(Ph.D)\n" +
                "\n" +
                "   رشته تحصيلي : تاریخ ایران (بعد از اسلام) \n" +
                "\n" +
                "  دانشگاه محل تحصیل: واحد علوم تحقیقات تهران\n" +
                "\n" +
                "     كشور محل تحصيل : ایران ";
        //////////////////////////////////// DataBase

        final TestAdapter mDbHelper = new TestAdapter(Riasat.this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor daneshkadeCursur = mDbHelper.getTestData("*", "tbl_boss");
        Log.e("ttt", daneshkadeCursur.getCount() + "");
        String txthtml = "";
        ImageView imageView = (ImageView) findViewById(R.id.imageView3);
        try {
            txthtml = daneshkadeCursur.getString(1);

            byte[] image = daneshkadeCursur.getBlob(2);


            byte[] decodedString = new byte[image.length];
            for (int i = 0; i < decodedString.length; i++) {
                decodedString[i] = image[i];
            }
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                imageView.setImageResource(R.drawable.riasat);

            } else {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {

            imageView.setImageResource(R.drawable.riasat);
        }

        //////////////////Set Font to MAIN TEXT
        TextView mainMatn = (TextView) findViewById(R.id.textViewR);
        //  mainMatn.setText(txt);
        mainMatn.setText(Html.fromHtml(txthtml));
        Typeface font = Typeface.createFromAsset(getAssets(), "BNazanin.ttf");
        mainMatn.setTypeface(font);

        ////////////////set animation to main text (Linear Layout)
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.matnLayoutR);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
        mainLayout.startAnimation(animation);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewR);
        Animation animationFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation3);
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


}
