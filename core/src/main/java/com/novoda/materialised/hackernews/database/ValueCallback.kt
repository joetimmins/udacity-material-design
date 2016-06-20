package com.novoda.materialised.hackernews.database

interface ValueCallback<T> {
    fun onValueRetrieved(value: T)
}
