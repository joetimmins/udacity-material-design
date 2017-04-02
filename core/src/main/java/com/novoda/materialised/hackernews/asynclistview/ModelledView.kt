package com.novoda.materialised.hackernews.asynclistview

interface ModelledView<T : ViewData<Any>> {
    fun updateWith(viewModel: ViewModel<T>)
}
