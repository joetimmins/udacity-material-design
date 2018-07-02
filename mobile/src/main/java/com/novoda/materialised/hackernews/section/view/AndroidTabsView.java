package com.novoda.materialised.hackernews.section.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.novoda.materialised.hackernews.Presenter;
import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import static com.novoda.materialised.hackernews.NullHandlerKt.handleNullable;

public final class AndroidTabsView implements TabsView<Section> {

    private final ViewPager sectionViewPager;
    private final TabLayout tabLayout;
    private final Function1<AsyncListView<StoryViewData>, Presenter<Section>> sectionPresenterFactory;

    public AndroidTabsView(ViewPager sectionViewPager, TabLayout tabLayout, Function1<AsyncListView<StoryViewData>, Presenter<Section>> factory) {
        this.sectionViewPager = sectionViewPager;
        this.tabLayout = tabLayout;
        this.sectionPresenterFactory = factory;
    }

    @Override
    public void updateWith(@NotNull List<ViewModel<Section>> viewModels) {
        PagerAdapter sectionPagerAdapter = new SectionPagerAdapter(viewModels, sectionPresenterFactory);
        sectionViewPager.setAdapter(sectionPagerAdapter);

        tabLayout.setupWithViewPager(sectionViewPager);
    }

    @Override
    public void refreshCurrentTab() {
        final int selectedTabPosition = tabLayout.getSelectedTabPosition();

        Function0<TabLayout.Tab> currentTabOrNull = () -> tabLayout.getTabAt(selectedTabPosition);
        TabLayout.Tab currentTab = handleNullable(currentTabOrNull, tabLayout.newTab());

        currentTab.select();
    }

}
