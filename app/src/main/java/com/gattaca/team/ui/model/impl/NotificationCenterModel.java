package com.gattaca.team.ui.model.impl;

import com.gattaca.team.annotation.NotifyEventTag;
import com.gattaca.team.annotation.NotifyType;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.ui.container.list.item.NotifyCenterListItem;
import com.gattaca.team.ui.model.IListContainerModel;

import java.util.List;

public final class NotificationCenterModel
        extends IListContainerModel<NotifyEventObject, NotifyCenterListItem> {

    public NotificationCenterModel(List<NotifyEventObject> list) {
        super(list);
    }

    @Override
    protected void convert() {
        NotifyCenterListItem object;
        for (NotifyEventObject item : getDataItems()) {
            object = new NotifyCenterListItem(item.getTime(), item.getModuleNameResId());
            switch (item.getEventType()) {
                case NotifyType.AD_again_less_140_90:
                    object.addText("AD_again_less_140_90");
                    break;
                case NotifyType.AD_again_more_140_90:
                    object.addText("AD_again_more_140_90");
                    object.addTag(NotifyEventTag.Red);
                    break;
                case NotifyType.AD_more_140_160:
                    object.addText("AD_more_140_160");
                    break;
                case NotifyType.AD_more_160_180:
                    object.addText("AD_more_160_180");
                    break;
                case NotifyType.AD_more_180:
                    object.addText("AD_more_180");
                    break;
                case NotifyType.AD_trouble:
                    object.addText("AD_trouble");
                    break;
                case NotifyType.BPM_less_40:
                    object.addText("BPM_less_40");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.BPM_less_50_more_40:
                    object.addText("BPM_less_50_more_40");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.BPM_more_100:
                    object.addText("BPM_more_100");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_2:
                    object.addText("PC_2");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_3:
                    object.addText("PC_3");
                    object.addTag(NotifyEventTag.Red);
                    break;
                case NotifyType.PC_Period_12_hours:
                    object.addText("PC_Period_12_hours");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_Period_15_min:
                    object.addText("PC_Period_15_min");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_Period_1_hour:
                    object.addText("PC_Period_1_hour");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_Period_24_hours:
                    object.addText("PC_Period_24_hours");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_Period_30_min:
                    object.addText("PC_Period_30_min");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_Period_5_min:
                    object.addText("PC_Period_5_min");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_more_limit_per_day:
                    object.addText("PC_more_limit_per_day");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.PC_more_limit_per_hour:
                    object.addText("PC_more_limit_per_hour");
                    object.addTag(NotifyEventTag.Yellow);
                    break;
                case NotifyType.Tracker_reminder:{
                    object.addText("Напоминание");
                }
            }
            addListItem(object);
        }
    }
}
