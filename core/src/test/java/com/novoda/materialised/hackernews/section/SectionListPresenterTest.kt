package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabView
import com.novoda.materialised.hackernews.stories.SpyingSectionPresenter
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test


class SectionListPresenterTest {

    @Test
    fun populateViewWithViewModelsFromProvider_whenStartingPresenting() {
        val view = SpyingTabView()
        val expectedList: List<ViewModel<Section>> = listOf(ViewModel(Section.BEST), ViewModel(Section.TOP_STORIES))
        val sectionListPresenter = SectionListPresenter(DummySectionProvider(), view, SpyingSectionPresenter())

        sectionListPresenter.startPresenting()

        assertThat(view.receivedList).isEqualTo(expectedList)
    }

    @Test
    fun clickingViewModelStartsPresentingThatSection() {
        val spyingPresenter = SpyingSectionPresenter()
        val view = SpyingTabView()
        val sectionListPresenter = SectionListPresenter(DummySectionProvider(), view, spyingPresenter)

        sectionListPresenter.startPresenting()
        view.receivedList.last().onClick()

        assertThat(spyingPresenter.presentedTypes.last()).isEqualTo(Section.TOP_STORIES)
    }
}

class DummySectionProvider : SectionProvider {
    override fun provideSections(): List<Section> {
        return listOf(Section.BEST, Section.TOP_STORIES)
    }
}

class SpyingTabView : TabView<Section> {
    var receivedList: List<ViewModel<Section>> = emptyList()

    override fun updateWith(viewModels: List<ViewModel<Section>>) {
        receivedList = viewModels
    }

}
