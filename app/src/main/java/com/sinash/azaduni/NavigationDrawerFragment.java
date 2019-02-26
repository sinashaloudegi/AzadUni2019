package com.sinash.azaduni;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView l1;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer = true;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        //  ActionBar actionBar = getActionBar();
        //  actionBar.setIcon(R.drawable.sms4);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());
        alertDialog.setTitle("خروج از برنامه");
        alertDialog.setMessage("آیا مایل به خروج هستید؟");
        alertDialog.setPositiveButton("بلی",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Exit me", true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        alertDialog.setNegativeButton("خیر",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final TestAdapter mDbHelper = new TestAdapter(getActivity());
        mDbHelper.createDatabase();
        mDbHelper.open();

        DataBaseHelper mDb = new DataBaseHelper(getActivity());
        SQLiteDatabase m = mDb.getReadableDatabase();

        Cursor allnewsCursur = mDbHelper.getTestData("*", "tbl_news", "seen", "0");
        int cursurCount = allnewsCursur.getCount();

        l1 = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        final ImageView[] mOverLayImage = new ImageView[1];
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent goToMain = new Intent(getActivity(), MainPage.class);
                        startActivity(goToMain);
                        getActivity().finish();
                        break;
                    case 1:
                        Intent goToRiasat = new Intent(getActivity(), Riasat.class);
                        startActivity(goToRiasat);


                        break;
                    case 2:

                        Intent goToMoavenat = new Intent(getActivity(), Moavenat.class);
                        startActivity(goToMoavenat);

                        break;
                    case 3:
                        Intent goToDaneshkade = new Intent(getActivity(), Daneshkade.class);
                        startActivity(goToDaneshkade);


                        break;
                    // case 4:
                    /// barname darsi
                    //     Intent goToBarname = new Intent(getActivity(), JostojooBarname.class);
                    // startActivity(goToBarname);
                    //     Toast.makeText(getActivity(),"این امکان در نسخه های بعدی فعال خواهد شد",Toast.LENGTH_SHORT).show();

                    //     break;
                    case 4:
                        Intent goToPazhohesh = new Intent(getActivity(), Pazhoheshkadeh.class);
                        startActivity(goToPazhohesh);
                        break;
                    case 5:
                        Intent goToAz = new Intent(getActivity(), AzmayeshGahHa.class);
                        startActivity(goToAz);
                        break;
                    case 6:
                        Intent goToHam = new Intent(getActivity(), HamayeshVaMosabeghat.class);
                        startActivity(goToHam);
                        break;
                    case 7:
                        Intent goToAkhbar = new Intent(getActivity(), NewsList.class);
                        startActivity(goToAkhbar);

                        break;
                    case 8:
                        Intent goToTelefon = new Intent(getActivity(), Phone.class);
                        startActivity(goToTelefon);

                        break;
                    case 9:
                        Intent goToLink = new Intent(getActivity(), Link.class);
                        goToLink.putExtra("ForL", "L");
                        startActivity(goToLink);

                        break;
                    case 10:
                        /////////////////////////////////////////////////////////////////////SERVICE

                        Context ctx = getActivity();
/** this gives us the time for the first trigger.  */
                        Calendar cal = Calendar.getInstance();
                        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
                        long interval = 1000 * 30 * 1; // 5 minutes in milliseconds
                        Intent serviceIntent = new Intent(ctx, MyService.class);
// make sure you **don't** use *PendingIntent.getBroadcast*, it wouldn't work
                        PendingIntent servicePendingIntent =
                                PendingIntent.getService(ctx,
                                        5, // integer constant used to identify the service
                                        serviceIntent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);  // FLAG to avoid creating a second service if there's already one running
// there are other options like setInexactRepeating, check the docs
                        am.setRepeating(
                                AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                                cal.getTimeInMillis(),
                                interval,
                                servicePendingIntent
                        );


                        //////////////////////////////////////////////////////////////////////
                        Intent goToAbout = new Intent(getActivity(), About.class);
                        startActivity(goToAbout);

                        break;
                    case 11:
                        if (isInternet()) {
                            Intent goToUpdate = new Intent(getActivity(), Update.class);
                            startActivity(goToUpdate);
                        } else {
                            Toast.makeText(getActivity(), "لطفا به اینترنت متصل شوید", Toast.LENGTH_LONG).show();
                        }

                        break;


                    case 12:
                        backButtonHandler();
                        break;

                }
                selectItem(position);
            }
        });
        l1.setAdapter(new CustomList(
                getActivity(),
                new String[]{
                        "دانشگاه", "ریاست", "معاونت ها", "دانشکده ها", "پژوهشکده ها", "آزمایشگاه ها", "همایش ها و مسابقات", "اخبار", "دفترچه تلفن", "لینک های مفید ", "درباره ما", "به روز رسانی", "خروج"
                }, new Integer[]{R.drawable.daneshgahicon, R.drawable.riyasaticon, R.drawable.moavenathaicon, R.drawable.daneshkadehaicon, R.drawable.azicon, R.drawable.azmaicon, R.drawable.iconhamvamos, R.drawable.akhbaricon, R.drawable.telefonicon, R.drawable.linkicon, R.drawable.darbaremaicon, R.drawable.beroozresaniicon, R.drawable.khoroojicon},
                new String[]{
                        "n", "n", "n", "n", "n", "n", "n", cursurCount + "", "n", "n", "n", "n", "n"
                }));
        l1.setItemChecked(mCurrentSelectedPosition, true);
        return l1;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.icondrawer);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#22497d")));


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.icondrawer,       /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                //  getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;

        if (l1 != null) {
            l1.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
///////////////////////////


    //////////////////////


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == 3) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();

//actionBar.setIcon(R.drawable.sms4);
//        actionBar.setLogo(R.drawable.sms4);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#22497d")));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private boolean isInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

