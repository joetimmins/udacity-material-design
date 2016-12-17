package com.novoda.materialised.hackernews.stories.database

interface StoryIdDatabase {
    fun readStoryIds(storyType: String, callback: ValueCallback<List<Long>>)
}
