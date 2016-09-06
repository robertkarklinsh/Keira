package com.gattaca.team.keira.data.datamanager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import com.gattaca.team.keira.BuildConfig;
import com.gattaca.team.keira.ui.activity.MainActivity;

/**
 * Created by Robert on 21.08.2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class DataManagerTest {

    Activity activity;

    @Before
    public void setup() {
        activity = new Activity();
    }

    @Test
    public void testMethod() {

    }


}
