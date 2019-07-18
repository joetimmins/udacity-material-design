package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.UiState
import com.novoda.materialised.hackernews.section.view.TabsView

class AllSectionsPresenter(private val provider: SectionListProvider,
                           private val view: TabsView<Section>) {
    fun startPresenting() {
        val sections = provider.provideSections()
        val sectionViewModels = sections
            .map { section -> UiState(viewData = section) }
        view.updateWith(sectionViewModels)
    }

    fun resumePresenting() {
        view.refreshCurrentTab()
    }
}
