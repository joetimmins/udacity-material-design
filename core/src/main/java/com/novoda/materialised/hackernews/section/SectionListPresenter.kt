package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabView

class SectionListPresenter(val provider: SectionProvider,
                           val view: TabView<Section>,
                           val sectionPresenter: Presenter<Section>) {
    fun startPresenting() {
        val sectionViewModels = provider.provideSections()
                .map { ViewModel(it, { sectionPresenter.present(it) }) }
        view.updateWith(sectionViewModels)
    }
}
