package com.novoda.materialised.hackernews.topstories.database

interface TopStoriesDatabase {
    fun readTopStoriesIds(callback: ValueCallback<List<Long>>)
}
