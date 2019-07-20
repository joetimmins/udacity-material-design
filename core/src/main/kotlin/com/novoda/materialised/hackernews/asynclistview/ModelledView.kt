package com.novoda.materialised.hackernews.asynclistview

interface ModelledView<T : Any> {
    fun updateWith(uiState: UiState<T>)
}
