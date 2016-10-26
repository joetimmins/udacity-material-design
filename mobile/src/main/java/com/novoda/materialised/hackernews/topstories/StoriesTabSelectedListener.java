package com.novoda.materialised.hackernews.topstories;

import android.support.design.widget.TabLayout;

class StoriesTabSelectedListener implements TabLayout.OnTabSelectedListener {

    private final TabPresenter tabPresenter;

    StoriesTabSelectedListener(TabPresenter tabPresenter) {
        this.tabPresenter = tabPresenter;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabPresenter.tabSelected(tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // no-op
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // no-op
    }
}
