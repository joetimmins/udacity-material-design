package com.novoda.materialised.hackernews.asynclistview

interface ModelledView<T : UiData<Any>> {
    fun updateWith(uiState: UiState<T>)
}
