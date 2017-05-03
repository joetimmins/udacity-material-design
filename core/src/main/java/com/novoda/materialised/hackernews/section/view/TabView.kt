package com.novoda.materialised.hackernews.section.view

import com.novoda.materialised.hackernews.asynclistview.ViewData
import com.novoda.materialised.hackernews.asynclistview.ViewModel

interface TabView<T : ViewData<String>> {
    fun updateWith(viewModels: List<ViewModel<T>>)
    fun refreshCurrentTab()
}
