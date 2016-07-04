package com.novoda.materialised.hackernews.generics

interface DecoupledViewModel<T: ViewData> {
    val viewData: T
    val viewBehaviour: ClickListener<T>
}
