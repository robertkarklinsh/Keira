package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.impl.DataBankContainer;
import com.gattaca.team.ui.container.impl.MonitorContainer;
import com.gattaca.team.ui.container.impl.NotificationCenterContainer;
import com.gattaca.team.ui.container.impl.SettingsContainer;
import com.gattaca.team.ui.container.impl.TrackerContainer;
import com.gattaca.team.ui.dialog.DialogFactory;
import com.gattaca.team.ui.model.IContainerModel;
import com.gattaca.team.ui.model.impl.DataBankModel;
import com.gattaca.team.ui.model.impl.NotificationCenterModel;
import com.gattaca.team.ui.model.impl.TrackerModel;
import com.gattaca.team.ui.utils.ActivityTransferData;
import com.gattaca.team.ui.utils.ContainerTransferData;
import com.gattaca.team.ui.utils.DialogTransferData;
import com.gattaca.team.ui.utils.MainMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.otto.Subscribe;

import io.realm.Realm;

public final class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    private IContainer
            trackerContainer,
            notificationCenterContainer,
            monitorContainer,
            dataBankContainer,
            settingsContainer,
            currentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        trackerContainer = new TrackerContainer(this);
        notificationCenterContainer = new NotificationCenterContainer(this);
        monitorContainer = new MonitorContainer(this);
        dataBankContainer = new DataBankContainer(this);
        settingsContainer = new SettingsContainer(this);

        final DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withOnDrawerItemClickListener(this);
        for (MainMenu item : MainMenu.values()) {
            drawerBuilder.addDrawerItems(
                    new PrimaryDrawerItem()
                            .withName(item.getNameId())
                            .withIcon(item.getIconId()));
        }
        drawerBuilder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentContainer == null) {
            requestChangeToNewContainer(new ContainerTransferData(MainMenu.values()[0]));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentContainer.changeCurrentVisibilityState(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentContainer.changeCurrentVisibilityState(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(currentContainer.getMenuItemActions(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        currentContainer.onMenuItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        requestChangeToNewContainer(new ContainerTransferData(MainMenu.values()[position]));
        return false;
    }

    @Subscribe
    /**
     * For open new container from any ui place
     * */
    public void requestChangeToNewContainer(final ContainerTransferData item) {
        if (currentContainer != null) {
            currentContainer.changeCurrentVisibilityState(true);
        }
        switch (item.getMenuItemForOpen()) {
            case Notification:
                currentContainer = notificationCenterContainer;
                break;
            case Tracker:
                currentContainer = trackerContainer;
                break;
            case Monitor:
                currentContainer = monitorContainer;
                break;
            case DataBank:
                currentContainer = dataBankContainer;
                break;
            case Settings:
                currentContainer = settingsContainer;
                break;
        }
        forceRedrawCurrentContainer(true);
    }

    @Subscribe
    public void listenerForNewActivityRequest(ActivityTransferData request) {
        request.launchRequestedActivity(this);
    }

    @Override
    protected void onDestroy() {
        Realm.getDefaultInstance().close();
        super.onDestroy();
    }

    @Subscribe
    public void updateTrackerPercents(int[] percents) {

    }

    @Subscribe
    public void eventCome(NotifyEventObject a) {
        currentContainer.eventCome(a);
    }

    @Subscribe
    public void showDialog(DialogTransferData a) {
        DialogFactory.createAndShowDialog(this, a);
    }

    @Subscribe
    public void forceRedrawCurrentContainer(Boolean a) {
        IContainerModel model = null;
        if (currentContainer == notificationCenterContainer) {
            model = new NotificationCenterModel(RealmController.getAllEvents());
        } else if (currentContainer == trackerContainer) {
            model = new TrackerModel(RealmController.getCurrentWeek());
        } else if (currentContainer == dataBankContainer) {
            model = new DataBankModel(RealmController.getAllSessions());
        }
        /*
        else if(currentContainer == monitorContainer){
        }
        else if(currentContainer == settingsContainer){
        }*/
        currentContainer.changeCurrentVisibilityState(false);
        currentContainer.reDraw(model);
        invalidateOptionsMenu();
    }
}
