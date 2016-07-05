package com.novoda.materialised.hackernews.topstories.database

import com.novoda.materialised.hackernews.topstories.database.ValueCallback

interface TopStoriesDatabase {
    fun readAll(callback: ValueCallback<List<Long>>)
}
