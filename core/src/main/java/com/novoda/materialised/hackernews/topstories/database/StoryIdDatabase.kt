package com.novoda.materialised.hackernews.topstories.database

interface StoryIdDatabase {
    fun readTopStoriesIds(callback: ValueCallback<List<Long>>)
}
