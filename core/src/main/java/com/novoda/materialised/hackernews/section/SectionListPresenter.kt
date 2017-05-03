package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabView

class SectionListPresenter(val provider: SectionProvider,
                           val view: TabView<Section>) {
    fun present() {
        val sectionViewModels = provider.provideSections().map { section -> ViewModel(section) }
        view.updateWith(sectionViewModels)
    }
}
