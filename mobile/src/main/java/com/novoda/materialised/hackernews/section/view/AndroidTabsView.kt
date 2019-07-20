package com.novoda.materialised.hackernews.section.view

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.UiModel
import com.novoda.materialised.hackernews.onNullOrExceptionReturn
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.view.UiStory

class AndroidTabsView(
    private val sectionViewPager: ViewPager,
    private val tabLayout: TabLayout,
    private val sectionPresenterFactory: Function1<AsyncListView<UiStory>, Presenter<Section>>
) : TabsView {

    override fun updateWith(uiModels: List<UiModel<Section>>) {
        val sectionPagerAdapter = SectionPagerAdapter(uiModels, sectionPresenterFactory)
        sectionViewPager.adapter = sectionPagerAdapter

        tabLayout.setupWithViewPager(sectionViewPager)
    }

    override fun refreshCurrentTab() {
        val selectedTabPosition = tabLayout.selectedTabPosition

        val currentTab = { tabLayout.getTabAt(selectedTabPosition) }
            .onNullOrExceptionReturn(tabLayout.newTab())

        currentTab.select()
    }
}
