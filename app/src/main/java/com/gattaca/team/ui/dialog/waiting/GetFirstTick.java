package com.gattaca.team.ui.dialog.waiting;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gattaca.team.R;
import com.gattaca.team.ui.dialog.DialogObjectBase;


public final class GetFirstTick extends DialogObjectBase {
    @Override
    protected void applyDialogParams(MaterialDialog.Builder builder, Context context) {
        builder.title(R.string.global_waiting_header)
                .content(R.string.startFirstTick_body)
                .progress(true, 0)
                .cancelable(true);
    }
}
