package com.novoda.materialised.hackernews.stories;

import android.support.design.widget.TabLayout;

final class SectionSelectedListener implements TabLayout.OnTabSelectedListener {

    private final SectionPresenter sectionPresenter;

    SectionSelectedListener(SectionPresenter sectionPresenter) {
        this.sectionPresenter = sectionPresenter;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        sectionPresenter.tabSelected(tab.getText());
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
