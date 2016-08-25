package com.gattaca.bitalinoecgchart.tracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.R;

public class TestTabsActivity extends AppCompatActivity {

    static String[] days = new String[] {"пн",
            "вт",
            "ср",
            "чт",
            "пт",
            "сб",
            "вс"};

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tabs);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HI");


        ;
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(mViewPager);
        int k = tabLayout.getSelectedTabPosition();



        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(R.layout.custom_tab_layout);
            TextView tx = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.custom_tab_text);
            tx.setText(days[i]);
            tx.setTextColor(Color.WHITE);

        }
        ImageView img = (ImageView) tabLayout.getTabAt(k).getCustomView().findViewById(R.id.custom_tab_img);
        img.setImageResource(R.drawable.dial_ex);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//               tab.setIcon(R.drawable.dial_ex);
                ImageView img = (ImageView) tab.getCustomView().findViewById(R.id.custom_tab_img);
                img.setImageResource(R.drawable.dial_ex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.setIcon(R.drawable.dial);
                ImageView img = (ImageView) tab.getCustomView().findViewById(R.id.custom_tab_img);
                img.setImageResource(R.drawable.dial);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                tab.setIcon(R.drawable.dial_ex);
                ImageView img = (ImageView) tab.getCustomView().findViewById(R.id.custom_tab_img);
                img.setImageResource(R.drawable.dial_ex);

            }
        });


        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        final LinearLayout ll = (LinearLayout) findViewById(R.id.ll1);


        AppBarLayout a = (AppBarLayout) findViewById(R.id.appbar);
        final TabLayout tb = (TabLayout) findViewById(R.id.tabs1);

        a.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                tb.setAlpha(1f - percentage * percentage * 10);
//                toolbar.setAlpha(percentage);
                ll.setAlpha(1f - percentage * percentage * 10);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        String[] myDataset = new String[]{"aa","bb","cc","cc","cc","aa","bb","cc"};

        public PlaceholderFragment() {
        }

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_test_tabs, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            View rootView = inflater.inflate(R.layout.activity_test_expand_tabs, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(container.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new MyAdapter(myDataset, mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "пн";
//                case 1:
//                    return "вт";
//                case 2:
//                    return "SECTION 3";
//                case 3:
//                    return "SECTION 3";
//                case 4:
//                    return "SECTION 3";
//                case 5:
//                    return "SECTION 3";
//                case 6:
//                    return "SECTION 3";
//            }
            return null;
        }
    }
}
