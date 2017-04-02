package com.novoda.materialised.hackernews.asynclistview


data class ViewModel<T : ViewData<Any>>(
        val viewData: T,
        val viewBehaviour: ClickListener<T>
)
