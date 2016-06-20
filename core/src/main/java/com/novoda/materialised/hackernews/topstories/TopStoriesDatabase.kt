package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.ValueCallback

interface TopStoriesDatabase {
    fun readAll(callback: ValueCallback<List<Long>>)
}
