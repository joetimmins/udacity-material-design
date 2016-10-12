package com.novoda.materialised.hackernews.topstories.database

interface StoryIdDatabase {
    fun readStoryIds(storyType: String, callback: ValueCallback<List<Long>>)
}
