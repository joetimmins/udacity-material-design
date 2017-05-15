package com.novoda.materialised.hackernews.section.view;

import android.support.design.widget.TabLayout;

import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.section.Section;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.functions.Function0;

import static com.novoda.materialised.hackernews.NullHandlerKt.handleNullable;

public final class AndroidTabsView implements TabsView<Section> {

    private final TabLayout tabLayout;

    public AndroidTabsView(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public void updateWith(@NotNull List<ViewModel<Section>> viewModels, @NotNull final ViewModel<Section> defaultValue) {
        for (ViewModel<Section> viewModel : viewModels) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(viewModel.getViewData().getUserFacingName());
            tab.setTag(viewModel);
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                ViewModel<Section> viewModel = handleNullable(nullableViewModelFrom(tab), defaultValue);
                viewModel.onClick();
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
    public void refreshCurrentTab(@NotNull ViewModel<Section> defaultValue) {
        final int selectedTabPosition = tabLayout.getSelectedTabPosition();

        TabLayout.Tab currentTab = handleNullable(nullableTabAt(selectedTabPosition), tabLayout.newTab());

        ViewModel<Section> viewModel = handleNullable(nullableViewModelFrom(currentTab), defaultValue);
        viewModel.onClick();
    }

    @Nullable
    private Function0<ViewModel<Section>> nullableViewModelFrom(final TabLayout.Tab tab) {
        return new Function0<ViewModel<Section>>() {
            @Override
            public ViewModel<Section> invoke() {
                return (ViewModel<Section>) tab.getTag();
            }
        };
    }

    @Nullable
    private Function0<TabLayout.Tab> nullableTabAt(final int selectedTabPosition) {
        return new Function0<TabLayout.Tab>() {
            @Override
            public TabLayout.Tab invoke() {
                return tabLayout.getTabAt(selectedTabPosition);
            }
        };
    }
}