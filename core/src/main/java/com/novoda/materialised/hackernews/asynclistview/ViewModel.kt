package com.novoda.materialised.hackernews.asynclistview

interface ViewModel<out M, N : ViewData<M>> {
    val viewData: N
    val viewBehaviour: ClickListener<N>
}
