package com.novoda.materialised.hackernews.asynclistview

data class ViewModel<T : ViewData<Any>>(
        val viewData: T,
        private val viewBehaviour: (T) -> Unit = {}
) {
    fun onClick() {
        viewBehaviour(viewData)
    }
}
