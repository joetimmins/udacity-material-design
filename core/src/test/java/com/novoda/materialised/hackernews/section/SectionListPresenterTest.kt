package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabView
import com.novoda.materialised.hackernews.stories.SpyingSectionPresenter
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test


class SectionListPresenterTest {

    @Test
    fun populateViewWithViewDataFromProvider_whenStartingPresenting() {
        val view = SpyingTabView()
        val expectedViewData: List<Section> = listOf(Section.BEST, Section.TOP_STORIES)
        val sectionListPresenter = SectionListPresenter(DummySectionProvider(), view, SpyingSectionPresenter())

        sectionListPresenter.startPresenting()

        assertThat(view.receivedViewData).isEqualTo(expectedViewData)
    }

    @Test
    fun clickingViewModelStartsPresentingThatSection() {
        val spyingPresenter = SpyingSectionPresenter()
        val view = SpyingTabView()
        val sectionListPresenter = SectionListPresenter(DummySectionProvider(), view, spyingPresenter)

        sectionListPresenter.startPresenting()
        view.receivedViewModels.last().onClick()

        assertThat(spyingPresenter.presentedTypes.last()).isEqualTo(Section.TOP_STORIES)
    }
}

class DummySectionProvider : SectionProvider {
    override fun provideSections(): List<Section> {
        return listOf(Section.BEST, Section.TOP_STORIES)
    }
}

class SpyingTabView : TabView<Section> {
    var receivedViewModels: List<ViewModel<Section>> = emptyList()
    var receivedViewData: List<Section> = emptyList()

    override fun updateWith(viewModels: List<ViewModel<Section>>) {
        receivedViewModels = viewModels
        receivedViewData = viewModels.map { it.viewData }
    }

}
