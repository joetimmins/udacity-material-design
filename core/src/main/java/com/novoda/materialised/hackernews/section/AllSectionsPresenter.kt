package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabsView

class AllSectionsPresenter(private val provider: SectionListProvider,
                           private val view: TabsView<Section>) {
    fun startPresenting() {
        val sections = provider.provideSections()
        val sectionViewModels = sections
                .map { section -> ViewModel(section) }
        val defaultValue = sectionViewModels.first { (viewData) -> viewData.isDefault }
        view.updateWith(sectionViewModels, defaultValue)
    }

    fun resumePresenting() {
        val sections = provider.provideSections()
        val defaultValue = sections.first { section -> section.isDefault }
        view.refreshCurrentTab(ViewModel(defaultValue))
    }
}
