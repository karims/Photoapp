package com.mintash.photofun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseObject;

import java.util.List;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, DetailFragment.OnFragmentInteractionListener, GridFragment.OnFragmentInteractionListener, RemoteDataTask.OnAsyncRequestComplete {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private QuoteDisplay quoteDisplay;
    private static final String KEY_URL = "picasso:url";
    private ProgressDialog mProgressDialog;
    private List<PhotoData> photoDataList = null;
    private int limit = 8;
    List<ParseObject> ob;
    RemoteDataTask remoteTask;

    private int skip = 0;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        //setup gridview
        remoteTask = new RemoteDataTask(this, "Latest", limit);
        remoteTask.execute();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.bringToFront();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        Log.i("Testing", "Item clicked" + getTabSelected(position + 1));
        GlobalInits.skip = 0;
        GlobalInits.category_selected = getTabSelected(position + 1);
        RemoteDataTask subTask = new RemoteDataTask(this, getTabSelected(position+1), GlobalInits.skip);
        subTask.execute();

    }

    public String getTabSelected(int position){
        switch (position) {
            case 1:
                return getString(R.string.latest);

            case 2:
                return getString(R.string.abs);

            case 3:
                return getString(R.string.animals);

            case 4:
                return getString(R.string.beauty);

            case 5:
                return getString(R.string.bnw);

            case 6:
                return getString(R.string.cars);

            case 7:
                return getString(R.string.nature);

            case 8:
                return getString(R.string.quotes);

            case 9:
                return getString(R.string.travel);
            default:
                return getString(R.string.latest);
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.latest);
                break;
            case 2:
                mTitle = getString(R.string.abs);
                break;
            case 3:
                mTitle = getString(R.string.animals);
                break;
            case 4:
                mTitle = getString(R.string.beauty);
                break;
            case 5:
                mTitle = getString(R.string.bnw);
                break;
            case 6:
                mTitle = getString(R.string.cars);
                break;
            case 7:
                mTitle = getString(R.string.nature);
                break;
            case 8:
                mTitle = getString(R.string.quotes);
                break;
            case 9:
                mTitle = getString(R.string.travel);
                break;
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(String uri){

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, DetailFragment.newInstance(uri))
                .commit();

    }

    @Override
    public void asyncResponse(List<PhotoData> photoDataList) {
        Bundle bundle = new Bundle();
        GridFragment photoGrid = new GridFragment();
        Log.i("Scrolling~~~", "In asyncResponse");
        photoGrid.setPhotoDataList(photoDataList);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containertest, photoGrid)
                .commit();
        Log.i("Photoarray: ", photoDataList.get(0).getPhoto_url());
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
