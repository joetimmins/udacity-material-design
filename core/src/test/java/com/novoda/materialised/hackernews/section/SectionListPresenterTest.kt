package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabView
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test


class SectionListPresenterTest {

    @Test
    fun populateViewWithViewModelsFromProvider_whenPresenting() {
        val view = SpyingTabView()
        val provider = DummySectionProvider()
        val expectedList: List<ViewModel<Section>> = listOf(ViewModel(Section.BEST), ViewModel(Section.TOP_STORIES))

        val sectionListPresenter = SectionListPresenter(provider, view)
        sectionListPresenter.present()

        assertThat(view.receivedList).isEqualTo(expectedList)
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
