package com.gattaca.team.keira;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Trace;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import com.gattaca.team.keira.data.datasource.realm.RealmEntity;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(AndroidJUnit4.class)
public class fhirIntegrationTest {


    private static final long LAUNCH_TIMEOUT = 2000;
    private static final String KEIRA_PACKAGE_NAME = "com.gattaca.team.keira";

    private static UiDevice mDevice;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @SdkSuppress(minSdkVersion = 23)
    @Before
    public void startMainActivityFromHomeScreen() {

        if (InstrumentationRegistry.getTargetContext()
                .checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }


        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(KEIRA_PACKAGE_NAME);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        //mDevice.wait(Until.hasObject(By.pkg(KEIRA_PACKAGE_NAME).depth(0)),
        //      LAUNCH_TIMEOUT);
        UiObject allowButton = mDevice.findObject(new UiSelector()
                .text("Allow"));

        if (allowButton.waitForExists(LAUNCH_TIMEOUT)) {
            try {
                allowButton.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                fail("Can not grant permissions!");
            }
        }


    }

    @Test
    public void xmlTest() {

        final int MY_PERMISSIONS = 1;

        Trace.beginSection("Starting fhirIntegrationTest...");

        Context appContext = InstrumentationRegistry.getContext();

        File tempFolder = null;
        try {
            tempFolder = testFolder.newFolder("realmdata");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Can not create temp folder!");
        }

        RealmConfiguration config = new RealmConfiguration.Builder(tempFolder).build();
        Realm realm = Realm.getInstance(config);

        String xmlString = null;

        try {
            InputStream is = appContext.getAssets().open("test_patient.xml");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 1024];
            int length;

            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            xmlString = os.toString(StandardCharsets.UTF_8.name());

        } catch (IOException e) {
            e.printStackTrace();
            fail("Can not read file!");
        }

        FhirContext fhirContext = FhirContext.forDstu3();
        IParser parser = fhirContext.newXmlParser();
        Bundle patientObject = parser.parseResource(Bundle.class, xmlString);

        final String strFromParser = parser.encodeResourceToString(patientObject);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmEntity savedPatient = realm.createObject(RealmEntity.class);
                savedPatient.setText(strFromParser);
            }
        });

        RealmEntity savedPatient = realm.where(RealmEntity.class).findFirst();
        String strFromRealm = savedPatient.getText();

      /*  Intent startMainActivity = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
        startMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        InstrumentationRegistry.getContext().startActivity(startMainActivity);*/

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path + "/output.xml");
        if (file.exists()) file.delete();

        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(strFromRealm.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
            fail("Can not white to file!");
        }

        file.setReadable(true, false);

        assertTrue("Patients do not match!", strFromRealm.equals(xmlString));
        Trace.endSection();

    }
}