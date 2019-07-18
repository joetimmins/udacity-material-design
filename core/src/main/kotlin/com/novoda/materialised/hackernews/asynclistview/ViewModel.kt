package com.novoda.materialised.hackernews.asynclistview

data class ViewModel<T : ViewData<Any>>(
    private val viewBehaviour: (T) -> Unit = {},
    val viewData: T
) {
    fun invokeBehaviour() {
        viewBehaviour(viewData)
    }
}
