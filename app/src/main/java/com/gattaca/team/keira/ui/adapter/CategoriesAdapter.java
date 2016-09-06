

package com.gattaca.team.keira.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gattaca.team.keira.R;
import com.gattaca.team.keira.ui.model.Category;

import java.util.ArrayList;

/**
 * Created by Robert on 28.08.2016.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    ArrayList<Category> categories;
    Context context;

    public CategoriesAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameCategory;
        public TextView itemCounter;


        public ViewHolder(View itemView) {
            super(itemView);
            nameCategory = ((TextView) itemView.findViewById(R.id.category_name));
            itemCounter = ((TextView) itemView.findViewById(R.id.item_counter));
        }
    }


    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return 0;
    }
}

