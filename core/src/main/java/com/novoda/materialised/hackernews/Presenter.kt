package com.novoda.materialised.hackernews

interface Presenter<in T> {
    fun present(type: T)
}
