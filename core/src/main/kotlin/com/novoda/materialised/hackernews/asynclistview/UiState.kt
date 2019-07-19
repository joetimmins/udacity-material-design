package com.novoda.materialised.hackernews.asynclistview

data class UiState<T : UiData<Any>>(
    private val behaviour: (T) -> Unit = {},
    val data: T
) {
    fun invokeBehaviour() {
        behaviour(data)
    }
}
