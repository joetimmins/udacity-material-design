package com.novoda.materialised.hackernews.asynclistview

interface ViewModel<out S, T : ViewData<S>> {
    val viewData: T
    val viewBehaviour: ClickListener<T>
}
