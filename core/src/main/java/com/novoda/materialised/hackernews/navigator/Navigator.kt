package com.novoda.materialised.hackernews.navigator

interface Navigator {
    fun navigateTo(uri: String) // this sucks, but Intent needs an Android Uri, not a Java one :/
}
