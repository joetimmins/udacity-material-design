package com.novoda.materialised.hackernews.asynclistview


data class ViewModel<T>(
        val viewData: T,
        val viewBehaviour: ClickListener<T>
)
