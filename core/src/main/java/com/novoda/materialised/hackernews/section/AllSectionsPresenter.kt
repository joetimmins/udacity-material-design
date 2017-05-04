package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabView

class AllSectionsPresenter(private val provider: SectionListProvider,
                           private val view: TabView<Section>,
                           private val sectionPresenter: Presenter<Section>) {
    fun startPresenting() {
        val sections = provider.provideSections()
        val sectionViewModels = sections
                .map { section -> ViewModel(section, sectionSelectedListener()) }
        view.updateWith(sectionViewModels)
        sectionViewModels.first().onClick()
    }

    private fun sectionSelectedListener(): (Section) -> Unit = { section -> sectionPresenter.present(section) }

    fun resumePresenting() {
        view.refreshCurrentTab()
    }
}
