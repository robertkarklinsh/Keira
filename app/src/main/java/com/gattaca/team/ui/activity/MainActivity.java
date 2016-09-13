package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.service.SensorData;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.otto.Subscribe;


public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.navigation_item_1)
                                .withIcon(R.mipmap.ic_launcher)
                                .withIdentifier(R.id.navigation_action_1),
                        new PrimaryDrawerItem()
                                .withName(R.string.navigation_item_2)
                                .withIcon(R.mipmap.ic_launcher)
                                .withIdentifier(R.id.navigation_action_2),
                        new PrimaryDrawerItem()
                                .withName(R.string.navigation_item_3)
                                .withIcon(R.mipmap.ic_launcher)
                                .withIdentifier(R.id.navigation_action_3),
                        new PrimaryDrawerItem()
                                .withName(R.string.navigation_item_4)
                                .withIcon(R.mipmap.ic_launcher)
                                .withIdentifier(R.id.navigation_action_4)
                )
                .withOnDrawerItemClickListener(this)
                .build();
    }

    @Subscribe
    public void tickSensorData(SensorData data) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.countTicks(); i++) {
            builder.append("\ntimestump=").append(data.getTimeStump(i));
            for (int j = 0; j < data.getChannels(); j++) {
                builder.append("#").append(j).append("=").append(data.getVoltByChannel(i, j)).append("   ");
            }
            builder.append("\n");
        }
        Log.i(getClass().getSimpleName(), builder.toString());
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(builder.toString());
            }
        });*/
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

        return true;
    }
}
