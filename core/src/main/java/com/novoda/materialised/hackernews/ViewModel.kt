package com.novoda.materialised.hackernews

interface ViewModel<T> {
    val id: Int
    val clickListener: ClickListener<T>
}
