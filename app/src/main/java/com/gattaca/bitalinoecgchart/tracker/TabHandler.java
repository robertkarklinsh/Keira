package com.gattaca.bitalinoecgchart.tracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gattaca.team.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artem on 25.08.2016.
 */
public class TabHandler {

    static String[] days = new String[]{"пн",
            "вт",
            "ср",
            "чт",
            "пт",
            "сб",
            "вс"};

    public void initialize(TabLayout tabLayout, final ViewPager mViewPager) {

        tabLayout.setupWithViewPager(mViewPager);
        int k = tabLayout.getSelectedTabPosition();


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(R.layout.tracker_custom_tab_layout);
            TextView tx = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.custom_tab_text);
            tx.setText(days[i]);
            tx.setTextColor(Color.WHITE);

        }
        ImageView img = (ImageView) tabLayout.getTabAt(k).getCustomView().findViewById(R.id.tracker_custom_tab_img);
        img.setImageResource(R.drawable.dial_ex);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//               tab.setIcon(R.drawable.dial_ex);

                ImageView img = (ImageView) tab.getCustomView().findViewById(R.id.tracker_custom_tab_img);
                img.setImageResource(R.drawable.dial_ex);
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ImageView img = (ImageView) tab.getCustomView().findViewById(R.id.tracker_custom_tab_img);
                img.setImageResource(R.drawable.dial);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                ImageView img = (ImageView) tab.getCustomView().findViewById(R.id.tracker_custom_tab_img);
                img.setImageResource(R.drawable.dial_ex);
            }
        });

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Map<Integer, PlaceholderFragment> fragment = new HashMap<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PlaceholderFragment placeholderFragment = fragment.get(position);
            if (placeholderFragment == null) {
                placeholderFragment = PlaceholderFragment.newInstance(position + 1);
                fragment.put(position, placeholderFragment);
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return placeholderFragment;
        }

        @Override
        public int getCount() {

            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
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
        private int position = -1;

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
            if (this.getArguments() != null) {
                position = (int) this.getArguments().get(ARG_SECTION_NUMBER);
            }

            View rootView = inflater.inflate(R.layout.activity_test_expand_tabs, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(container.getContext()) {
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return true;
                }
            };
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new MyAdapter(mRecyclerView, position);
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }
    }

}
