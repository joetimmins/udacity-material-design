package com.novoda.materialised.hackernews.topstories.database

import com.novoda.materialised.hackernews.generics.ValueCallback

interface TopStoriesDatabase {
    fun readAll(callback: com.novoda.materialised.hackernews.generics.ValueCallback<List<Long>>)
}
