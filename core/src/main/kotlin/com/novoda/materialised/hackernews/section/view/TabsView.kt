package com.novoda.materialised.hackernews.section.view

import com.novoda.materialised.hackernews.asynclistview.UiData
import com.novoda.materialised.hackernews.asynclistview.UiState

interface TabsView<T : UiData<String>> {
    fun updateWith(uiStates: List<UiState<T>>)
    fun refreshCurrentTab()
}
