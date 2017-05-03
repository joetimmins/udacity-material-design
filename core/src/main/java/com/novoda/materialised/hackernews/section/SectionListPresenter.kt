package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabView

class SectionListPresenter(val provider: SectionProvider,
                           val view: TabView<Section>,
                           val sectionPresenter: Presenter<Section>) {
    fun startPresenting() {
        val sections = provider.provideSections()
        val sectionViewModels = sections
                .map { section -> ViewModel(section, sectionSelectedListener()) }
        view.updateWith(sectionViewModels)
        sectionViewModels.first().onClick()
    }

    private fun sectionSelectedListener(): (Section) -> Unit = { section -> sectionPresenter.present(section) }
}
