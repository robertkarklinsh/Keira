package com.gattaca.team.ui.container.list;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.ui.view.TimeStump;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public final class DataBankListItem extends AbstractItem<DataBankListItem, DataBankListItem.ViewHolder> {
    private final long timeStumpStart, timeStumpFinish;

    public DataBankListItem(final long timeStumpStart, final long timeStumpFinish) {
        this.timeStumpStart = timeStumpStart;
        this.timeStumpFinish = timeStumpFinish;
    }

    @Override
    public int getType() {
        return R.id.list_item_data_bank;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_bank_data;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);
        final Resources res = viewHolder.text.getResources();
        viewHolder.text.setText(String.format(res.getString(R.string.bank_data_formatted_session_item),
                TimeStump.convert(timeStumpStart, "dd/MM HH:mm"),
                TimeStump.convert(timeStumpFinish, "dd/MM HH:mm")));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.session);
        }
    }
}
