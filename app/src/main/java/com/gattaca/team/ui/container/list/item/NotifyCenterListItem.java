package com.gattaca.team.ui.container.list.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.annotation.ModuleName;
import com.gattaca.team.annotation.NotifyEventTag;
import com.gattaca.team.ui.view.MarkerByNotifyTag;
import com.gattaca.team.ui.view.TimeStump;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.HashSet;
import java.util.List;

public final class NotifyCenterListItem extends AbstractItem<NotifyCenterListItem, NotifyCenterListItem.ViewHolder> {
    private final HashSet<Integer> tags = new HashSet<>();
    private final
    @ModuleName
    int moduleNameResId;
    private final long timeStump;
    private String text;

    public NotifyCenterListItem(final long timeStump,
                                final @ModuleName int moduleNameResId) {
        this.moduleNameResId = moduleNameResId;
        this.timeStump = timeStump;
    }

    public NotifyCenterListItem addText(String text) {
        this.text = text;
        return this;
    }

    public NotifyCenterListItem addTag(@NotifyEventTag int tag) {
        tags.add(tag);
        return this;
    }

    @Override
    public int getType() {
        return R.id.list_item_notify_center;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_notify_center;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);
        viewHolder.module.setText(moduleNameResId);
        viewHolder.time.setTime(timeStump);
        viewHolder.text.setText(text);
        for (@NotifyEventTag int a : tags) {
            if (a == NotifyEventTag.Yellow || a == NotifyEventTag.Red) viewHolder.tag.setTag(a);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView module, text;
        TimeStump time;
        MarkerByNotifyTag tag;

        public ViewHolder(View view) {
            super(view);
            time = (TimeStump) view.findViewById(R.id.time);
            module = (TextView) view.findViewById(R.id.module);
            text = (TextView) view.findViewById(R.id.text);
            tag = (MarkerByNotifyTag) view.findViewById(R.id.marker);
        }
    }
}
