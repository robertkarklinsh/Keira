package com.gattaca.team.ui.container;

import android.app.Activity;
import android.view.View;

import com.gattaca.team.ui.model.IContainerModel;

public abstract class IContainer<Model extends IContainerModel> {
    private final Class<Model> modelClass;
    private final int rootViewId;
    private Model model;
    private View rootView;

    protected IContainer(Class<Model> modelClass, final int rootViewId) {
        this.modelClass = modelClass;
        this.rootViewId = rootViewId;
    }

    protected abstract void bindView();

    protected abstract void reDraw();

    public final void bindActivity(final Activity screen) {
        rootView = screen.findViewById(rootViewId);
        bindView();
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
