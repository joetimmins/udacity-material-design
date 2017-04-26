package com.novoda.materialised.hackernews.asynclistview


interface AsyncListView<T : ViewData<Any>> {

    fun updateWith(initialViewModelList: List<ViewModel<T>>)
    fun updateWith(viewModel: ViewModel<T>)
    fun showError()
}
