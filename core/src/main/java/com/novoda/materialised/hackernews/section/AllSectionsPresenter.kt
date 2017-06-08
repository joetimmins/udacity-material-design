package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.view.TabsView

class AllSectionsPresenter(private val provider: SectionListProvider,
                           private val view: TabsView<Section>,
                           private val sectionPresenter: Presenter<Section>) {
    fun startPresenting() {
        val sections = provider.provideSections()
        val sectionViewModels = sections
                .map { section -> ViewModel(section, sectionSelectedListener) }
        view.updateWith(sectionViewModels, sectionViewModels.filter { (viewData) -> viewData.isDefault }.first())
        sectionViewModels.first().onClick()
    }

    val sectionSelectedListener = { section: Section -> sectionPresenter.present(section) }

    fun resumePresenting() {
        val sections = provider.provideSections()
        val defaultValue = sections.filter(Section::isDefault).first()
        view.refreshCurrentTab(ViewModel(defaultValue, sectionSelectedListener))
    }
}
