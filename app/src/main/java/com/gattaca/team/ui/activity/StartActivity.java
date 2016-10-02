package com.gattaca.team.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gattaca.team.R;
import com.gattaca.team.annotation.DialogId;
import com.gattaca.team.annotation.FakeMessage;
import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.service.fake.FakeDataController;
import com.gattaca.team.ui.dialog.DialogFactory;
import com.gattaca.team.ui.dialog.DialogObjectBase;
import com.gattaca.team.ui.utils.DialogTransferData;
import com.squareup.otto.Subscribe;

public final class StartActivity extends AppCompatActivity {
    DialogObjectBase dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!AppPref.FakeGeneration.getBool(false)) {
            dialog = DialogFactory.createDialog(new DialogTransferData(DialogId.GenerationData));
            dialog.show(this);
            FakeDataController.startGeneration();
        } else {
            next();
        }
    }

    private void next() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Subscribe
    public void generationStateChange(final @FakeMessage Integer state) {
        if (state == FakeMessage.Finish) {
            dialog.dissmis();
            next();
        }
    }
}
