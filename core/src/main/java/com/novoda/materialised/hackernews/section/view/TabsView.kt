package com.novoda.materialised.hackernews.section.view

import com.novoda.materialised.hackernews.asynclistview.ViewData
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.Section

interface TabsView<T : ViewData<String>> {
    fun updateWith(viewModels: List<ViewModel<T>>, defaultValue: ViewModel<T>)
    fun refreshCurrentTab(defaultValue: ViewModel<Section>)
}
