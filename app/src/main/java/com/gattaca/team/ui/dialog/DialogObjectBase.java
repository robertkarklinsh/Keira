package com.gattaca.team.ui.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

public abstract class DialogObjectBase<Me extends DialogObjectBase, Callback> {
    protected Callback callback;
    protected MaterialDialog dialog;

    protected abstract void applyDialogParams(final MaterialDialog.Builder builder, final Context context);

    public final MaterialDialog build(final Context context) {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        applyDialogParams(builder, context);
        dialog = builder.build();
        return dialog;

    }

    public final void show(final Context context) {
        build(context).show();
    }

    public final void dissmis() {
        dialog.dismiss();
    }

    public final Me addCallback(final Callback callback) {
        this.callback = callback;
        return (Me) this;
    }
}
