package com.novoda.materialised.hackernews.topstories

interface TypedPresenter<in T> {
    fun present(type: T)
}
