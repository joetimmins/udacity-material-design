package com.novoda.materialised.hackernews.database

interface TopStories {
    fun readAll(callback: ValueCallback<List<Int>>)
}
