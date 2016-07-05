package com.novoda.materialised.hackernews.asynclistview

interface ViewModel<T : ViewData> {
    val viewData: T
    val viewBehaviour: ClickListener<T>
}
