package com.novoda.materialised.hackernews.section.view

import com.novoda.materialised.hackernews.asynclistview.UiState
import com.novoda.materialised.hackernews.section.Section

interface TabsView {
    fun updateWith(uiStates: List<UiState<Section>>)
    fun refreshCurrentTab()
}
