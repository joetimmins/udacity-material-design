package com.novoda.materialised.hackernews.stories.provider

@Suppress("AddVarianceModifier") // in T would break the signature of methods taking a ValueCallback as a parameter
interface ValueCallback<T> {
    fun onValueRetrieved(value: T)
}

fun <T> valueCallbackOf(valueCallbackFunction: (T) -> Unit): ValueCallback<T> {
    return object : ValueCallback<T> {
        override fun onValueRetrieved(value: T) {
            valueCallbackFunction(value)
        }
    }
}
