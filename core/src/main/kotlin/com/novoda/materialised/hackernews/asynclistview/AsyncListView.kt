package com.novoda.materialised.hackernews.asynclistview

interface AsyncListView<T : UiData<Any>> : ModelledView<T> {
    fun showError(throwable: Throwable)
}
