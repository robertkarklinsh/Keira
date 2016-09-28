package com.gattaca.team.ui.container;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.ui.model.IContainerModel;

public abstract class IContainer<Model extends IContainerModel> {
    private final View rootView;
    protected Context context;
    private Model model;

    public IContainer(Activity screen, final int rootViewId) {
        this(screen.findViewById(rootViewId));
        this.context = screen;
    }

    private IContainer(View rootView) {
        this.rootView = rootView;
        this.bindView();
        changeCurrentVisibilityState(true);
    }

    protected abstract void bindView();

    protected abstract void reDraw();

    public int getMenuItemActions() {
        return R.menu.empty_toolbar_actions;
    }

    public void onMenuItemSelected(final int id) {
    }

    public final void reDraw(Model model) {
        this.model = model;
        reDraw();
    }

    public final View getRootView() {
        return this.rootView;
    }

    public void changeCurrentVisibilityState(final boolean isHide) {
        getRootView().setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    protected Model getModel() {
        return this.model;
    }

    protected Context getContext() {
        return context;
    }
}
