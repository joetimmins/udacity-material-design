package com.novoda.materialised.hackernews.generics

interface ViewModel<T: ViewData> {
    val viewData: T
    val viewBehaviour: ClickListener<T>
}
