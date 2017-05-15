package com.novoda.materialised.hackernews.stories.provider

interface ValueCallback<in T> {
    fun onValueRetrieved(value: T)
}

fun <T> valueCallbackOf(valueCallbackFunction: (T) -> Unit): ValueCallback<T> {
    return object : ValueCallback<T> {
        override fun onValueRetrieved(value: T) {
            valueCallbackFunction(value)
        }
    }
}
