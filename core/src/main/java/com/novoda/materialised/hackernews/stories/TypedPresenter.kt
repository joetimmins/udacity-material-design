package com.novoda.materialised.hackernews.stories

internal interface TypedPresenter<in T> {
    fun present(type: T)
}
