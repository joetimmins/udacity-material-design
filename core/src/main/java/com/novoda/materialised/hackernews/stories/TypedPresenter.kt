package com.novoda.materialised.hackernews.stories

interface TypedPresenter<in T> {
    fun present(type: T)
}
