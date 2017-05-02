package com.novoda.materialised.hackernews.stories.database

interface StoryIdDatabase {
    fun readStoryIds(storyType: StoryType, callback: ValueCallback<List<Long>>)
}
