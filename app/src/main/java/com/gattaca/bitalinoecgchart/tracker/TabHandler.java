package com.gattaca.bitalinoecgchart.tracker;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.R;

/**
 * Created by Artem on 25.08.2016.
 */
public class TabHandler {

    static String[] days = new String[] {"пн",
            "вт",
            "ср",
            "чт",
            "пт",
            "сб",
            "вс"};

    public void initialize(TabLayout tabLayout, ViewPager mViewPager) {

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

    }

}
