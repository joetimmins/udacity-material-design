package com.novoda.materialised.hackernews.asynclistview

data class ViewModel<T : ViewData<Any>>(
        val viewData: T,
        private val viewBehaviour: ClickListener<T>
) {
    fun onClick() {
        viewBehaviour.onClick(viewData)
    }
}
