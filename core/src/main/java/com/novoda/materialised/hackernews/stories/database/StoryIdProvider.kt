package com.novoda.materialised.hackernews.stories.database

import com.novoda.materialised.hackernews.section.Section

interface StoryIdProvider {
    fun readStoryIds(section: Section, callback: ValueCallback<List<Long>>)
}
