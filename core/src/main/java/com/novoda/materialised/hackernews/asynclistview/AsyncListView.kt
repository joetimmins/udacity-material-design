package com.novoda.materialised.hackernews.asynclistview


interface AsyncListView<T : ViewData<Any>> : ModelledView<T> {
    fun updateWith(initialViewModelList: List<ViewModel<T>>)
    fun showError()
}
