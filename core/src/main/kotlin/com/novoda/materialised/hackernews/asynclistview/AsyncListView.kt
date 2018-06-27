package com.novoda.materialised.hackernews.asynclistview


interface AsyncListView<T : ViewData<Any>> : ModelledView<T> {
    fun showError(throwable: Throwable)
}
