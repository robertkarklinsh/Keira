package com.gattaca.team.ui.container;

import android.app.Activity;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.ui.model.IContainerModel;

public abstract class IContainer<Model extends IContainerModel> {
    private final Class<Model> modelClass;
    private final View rootView;
    private Model model;

    public IContainer(Activity screen, Class<Model> modelClass, final int rootViewId) {
        this.modelClass = modelClass;
        this.rootView = screen.findViewById(rootViewId);
        this.bindView();
    }

    protected abstract void bindView();

    protected abstract void reDraw();

    public int getMenuItemActions() {
        return R.menu.empty_toolbar_actions;
    }

    public void onMenuItemSelected(final int id) {
    }

    public final Class<Model> getModelClass() {
        return this.modelClass;
    }

    public final void reDraw(Model model) {
        this.model = model;
        reDraw();
    }

    public final View getRootView() {
        return this.rootView;
    }

    protected Model getModel() {
        return this.model;
    }
}
