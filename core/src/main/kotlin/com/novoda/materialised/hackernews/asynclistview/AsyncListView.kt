package com.novoda.materialised.hackernews.asynclistview

interface AsyncListView<T : Any> : ModelledView<T> {
    fun showError(throwable: Throwable)
}
