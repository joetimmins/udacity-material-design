package com.novoda.materialised.hackernews.section.view;

import android.support.design.widget.TabLayout;

import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.section.Section;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class AndroidTabView implements TabView<Section> {

    private final TabLayout tabLayout;

    public AndroidTabView(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public void updateWith(@NotNull List<ViewModel<Section>> viewModels) {
        for (ViewModel<Section> viewModel : viewModels) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(viewModel.getViewData().getUserFacingName());
            tab.setTag(viewModel);
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewModel<Section> viewModel = (ViewModel<Section>) tab.getTag();
                if (viewModel != null) {
                    viewModel.onClick();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void refreshCurrentTab() {
        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        TabLayout.Tab currentTab = tabLayout.getTabAt(selectedTabPosition);
        if (currentTab != null) {
            ViewModel<Section> viewModel = (ViewModel<Section>) currentTab.getTag();
            if (viewModel != null) {
                viewModel.onClick();
            }
        }
    }
}
