package com.novoda.materialised.hackernews.section.view

import com.novoda.materialised.hackernews.asynclistview.UiModel
import com.novoda.materialised.hackernews.section.Section

interface TabsView {
    fun updateWith(uiModels: List<UiModel<Section>>)
    fun refreshCurrentTab()
}
