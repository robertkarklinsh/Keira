package com.gattaca.team.ui.utils;

import com.gattaca.team.annotation.DialogId;

public final class DialogTransferData {
    final
    @DialogId
    private int dialogId;
    final private Object bindData;

    public DialogTransferData(@DialogId int dialogId) {
        this(dialogId, null);
    }

    public DialogTransferData(@DialogId int dialogId, Object bindData) {
        this.dialogId = dialogId;
        this.bindData = bindData;
    }

    public Object getBindData() {
        return bindData;
    }

    public
    @DialogId
    int getDialogId() {
        return dialogId;
    }
}
