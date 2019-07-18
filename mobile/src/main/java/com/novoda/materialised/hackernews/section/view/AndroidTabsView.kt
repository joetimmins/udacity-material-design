package com.novoda.materialised.hackernews.section.view

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.UiState
import com.novoda.materialised.hackernews.onNullOrExceptionReturn
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.view.StoryUiData

class AndroidTabsView(
    private val sectionViewPager: ViewPager,
    private val tabLayout: TabLayout,
    private val sectionPresenterFactory: Function1<AsyncListView<StoryUiData>, Presenter<Section>>
) : TabsView<Section> {

    override fun updateWith(uiStates: List<UiState<Section>>) {
        val sectionPagerAdapter = SectionPagerAdapter(uiStates, sectionPresenterFactory)
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
