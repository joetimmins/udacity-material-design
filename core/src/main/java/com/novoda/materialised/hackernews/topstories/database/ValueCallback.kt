package com.novoda.materialised.hackernews.topstories.database

interface ValueCallback<in T> {
    fun onValueRetrieved(value: T)
}

fun <T> valueCallbackFor(valueCallbackFunction: (T) -> Unit): ValueCallback<T> {
    return object : ValueCallback<T> {
        override fun onValueRetrieved(value: T) {
            valueCallbackFunction(value)
        }
    }
}
