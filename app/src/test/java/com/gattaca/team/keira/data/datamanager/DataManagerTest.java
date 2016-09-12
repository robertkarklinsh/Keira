package com.gattaca.team.keira.data.datamanager;

import android.app.Activity;
import android.os.Build;

import com.gattaca.team.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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
