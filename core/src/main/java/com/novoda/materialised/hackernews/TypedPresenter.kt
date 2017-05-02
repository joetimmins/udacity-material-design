package com.novoda.materialised.hackernews

interface TypedPresenter<in T> {
    fun present(type: T)
}
