package com.novoda.materialised.hackernews.asynclistview

data class UiState<T : UiData<Any>>(
    private val viewBehaviour: (T) -> Unit = {},
    val viewData: T
) {
    fun invokeBehaviour() {
        viewBehaviour(viewData)
    }
}
