package com.gattaca.team.ui.dialog;

import android.content.Context;

import com.gattaca.team.annotation.DialogId;
import com.gattaca.team.ui.dialog.action.SessionActions;
import com.gattaca.team.ui.dialog.waiting.GenerationData;
import com.gattaca.team.ui.dialog.waiting.GetFirstTick;
import com.gattaca.team.ui.utils.DialogTransferData;

public class DialogFactory {
    public static void createAndShowDialog(final Context c, final DialogTransferData data) {
        final DialogObjectBase a = createDialog(data);
        a.show(c);
    }

    public static DialogObjectBase createDialog(final DialogTransferData data) {
        DialogObjectBase dialog = null;
        switch (data.getDialogId()) {
            case DialogId.SessionActions:
                dialog = new SessionActions((Long) data.getBindData());
                break;
            case DialogId.GenerationData:
                dialog = new GenerationData();
                break;
            case DialogId.StartFirstTick:
                dialog = new GetFirstTick();
                break;
        }
        return dialog;
    }
}
