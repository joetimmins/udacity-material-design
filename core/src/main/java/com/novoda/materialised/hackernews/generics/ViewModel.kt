package com.novoda.materialised.hackernews.generics

interface ViewModel<T> {
    val id: Int
    val clickListener: ClickListener<T>
}
