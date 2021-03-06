package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.UiModel
import com.novoda.materialised.hackernews.section.view.TabsView
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class AllSectionsPresenterTest {

    @Test
    fun populateViewWithViewDataFromProvider_whenPresentingStarts() {
        val view = SpyingTabsView()
        val expectedViewData: List<Section> = listOf(Section.BEST, Section.TOP_STORIES)
        val sectionListPresenter = newAllSectionsPresenter(view)

        sectionListPresenter.startPresenting()

        assertThat(view.receivedViewData).isEqualTo(expectedViewData)
    }

    @Test
    fun presenterTellsViewToRefreshCurrentTab_whenPresentingResumes() {
        val view = SpyingTabsView()
        val sectionListPresenter = newAllSectionsPresenter(view)

        sectionListPresenter.resumePresenting()

        assertThat(view.currentTabRefreshed).isTrue
    }

    private fun newAllSectionsPresenter(view: SpyingTabsView) = AllSectionsPresenter(DummySectionListProvider(), view)
}

private class DummySectionListProvider : SectionListProvider {
    override fun provideSections(): List<Section> = listOf(Section.BEST, Section.TOP_STORIES)
}

private class SpyingTabsView : TabsView {
    var receivedUiModels: List<UiModel<Section>> = emptyList()
    var receivedViewData: List<Section> = emptyList()
    var currentTabRefreshed: Boolean = false

    override fun updateWith(uiModels: List<UiModel<Section>>) {
        receivedUiModels = uiModels
        receivedViewData = uiModels.map { it.data }
    }

    override fun refreshCurrentTab() {
        currentTabRefreshed = true
    }
}
