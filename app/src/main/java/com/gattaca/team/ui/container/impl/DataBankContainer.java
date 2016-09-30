package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.os.CountDownTimer;
import android.text.format.DateUtils;

import com.gattaca.team.R;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.ui.container.IListContainer;
import com.gattaca.team.ui.container.list.item.TrackerMeasureListItem;
import com.gattaca.team.ui.model.impl.DataBankModel;

public final class DataBankContainer extends IListContainer<DataBankModel, TrackerMeasureListItem> {
    private CountDownTimer timer = null;

    public DataBankContainer(final Activity screen) {
        super(screen, R.id.container_data_bank_id);
    }

    @Override
    public void changeCurrentVisibilityState(final boolean isHide) {
        if (isHide) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } else {
            startTime();
        }
        super.changeCurrentVisibilityState(isHide);
    }

    @Override
    public void eventCome(NotifyEventObject a) {
        AppUtils.postToBus(a);
    }

    private void startTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(DateUtils.MINUTE_IN_MILLIS, DateUtils.SECOND_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                AppUtils.postToBus(1);
            }

            @Override
            public void onFinish() {
                startTime();
            }
        };
        timer.start();
    }
}
