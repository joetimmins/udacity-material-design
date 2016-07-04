package com.novoda.materialised.hackernews.navigator

interface Navigator {
    fun navigateTo(uri: String) // using String here sucks, but Intent needs an Android Uri, not a Java one :/
}
