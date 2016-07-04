package com.novoda.materialised.hackernews.generics

interface ValueCallback<T> {
    fun onValueRetrieved(value: T)
}

fun <T> valueCallbackFor(valueCallbackFunction: (T) -> Unit): com.novoda.materialised.hackernews.generics.ValueCallback<T> {
    return object : com.novoda.materialised.hackernews.generics.ValueCallback<T> {
        override fun onValueRetrieved(value: T) {
            valueCallbackFunction(value)
        }
    }
}
