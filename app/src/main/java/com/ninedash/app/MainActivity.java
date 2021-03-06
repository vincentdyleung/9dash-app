package com.ninedash.app;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.http.AndroidHttpClient;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements ActionBar.TabListener, RestaurantListFragment.OnBlockSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private String fbId;
    private String username;
    public static final String HOST = "http://ninedash.herokuapp.com/";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36";
    public static final int REST_LIST_FRAG = 0;
    public static final int REPORT_TIME_FRAG = 1;
    private AndroidHttpClient httpClient;
    private Activity activity;
    private String selectedRestaurant;

    MenuItem menuItem;
    String mReportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        fbId = getIntent().getStringExtra("fbId");
        username = getIntent().getStringExtra("name");

        activity = this;
        try {
            checkUserRegistered(fbId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void checkUserRegistered(final String fbId) throws URISyntaxException {
        final URI uri = new URI(HOST + "users/" + fbId);
        new HTTPHandler() {
            @Override
            public HttpUriRequest getHttpRequest() {
                return new HttpGet(uri);
            }
            @Override
            public void onResponse(JSONObject res) {
                try {
                    if (res.getInt("count") > 0) {
                        Toast.makeText(activity, "Logged in as " + username, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        new HTTPHandler() {
                            @Override
                            public HttpUriRequest getHttpRequest() {
                                String jsonString = "{\"fbid\":\"" + fbId + "\",\"name\":\"" + username + "\",\"point\":6000}";
                                URI uri = null;
                                try {
                                    uri = new URI(HOST + "users/");
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                HttpPost postRequest = new HttpPost(uri);
                                postRequest.setHeader("Content-Type", "application/json");
                                try {
                                    StringEntity params = new StringEntity(jsonString);
                                    postRequest.setEntity(params);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                return postRequest;
                            }

                            @Override
                            public void onResponse(JSONObject res) {
                                try {
                                    if (res.getString("fbid").equals(fbId)) {
                                        Toast.makeText(activity, "Successfully registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "Register error", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public String getFbId() {
        return fbId;
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public String getSelectedRestaurant() {
        return selectedRestaurant;
    }

    public void onBlockSelected(String id) {
        mReportData = id;
        mViewPager.setCurrentItem(REPORT_TIME_FRAG);
        ReportTimeFragment reportTimeFrag = new ReportTimeFragment(id);
        selectedRestaurant = id;
//        Bundle args = new Bundle();
//        args.putString("id", mReportData);
//        reportTimeFrag.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.add(R.id.fragment_container, reportTimeFrag, "ReportTime");
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return new RestaurantListFragment();
            }
            else if (position == 1) {
                return new ReportTimeFragment(null);
            }
            else {
                UserFragment userFragment = new UserFragment();
                userFragment.setDetails();
                return userFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**)
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
            //View view = inflater.inflate(R.layout.report_time,container,false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
            //return view;
        }
    }

    private class UserFragment extends Fragment {
        TextView usernameText;
        TextView pointsText;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_user, viewGroup, false);
            usernameText = (TextView) view.findViewById(R.id.username);
            pointsText = (TextView) view.findViewById(R.id.points);
            return view;
        }

        public void setDetails() {
            new HTTPHandler() {
                @Override
                public HttpUriRequest getHttpRequest()  {
                    HttpGet getRequest = null;
                    URI uri = null;
                    try {
                        uri = new URI(HOST + "users/" + fbId);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    getRequest = new HttpGet(uri);
                    return getRequest;
                }

                @Override
                public void onResponse(JSONObject res) {
                    try {
                        Integer points = res.getInt("point");
                        pointsText.setText(Integer.toString(points));
                        usernameText.setText(username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }

}
