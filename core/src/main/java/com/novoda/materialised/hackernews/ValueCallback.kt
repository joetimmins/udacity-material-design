package com.novoda.materialised.hackernews

interface ValueCallback<T> {
    fun onValueRetrieved(value: T)
}
