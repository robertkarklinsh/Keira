package com.gattaca.team.ui.dialog.action;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.dialog.DialogObjectBase;


public final class SessionActions extends DialogObjectBase implements MaterialDialog.ListCallback {
    private final long timeStart;

    public SessionActions(final long timeStart) {
        this.timeStart = timeStart;
    }

    @Override
    protected void applyDialogParams(MaterialDialog.Builder builder, Context context) {
        builder.title(context.getResources().getString(R.string.session_action_header))
                .items(R.array.Session_actions)
                .itemsCallback(this)
                .cancelable(true);
    }

    @Override
    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        dialog.dismiss();
        switch (which) {
            case 0:
                RealmController.removeSession(timeStart);
                MainApplication.uiBusPost(true);
                break;
            case 1:
                //TODO: view
                MainApplication.showToastNotImplemented();
                break;
        }
    }
}
