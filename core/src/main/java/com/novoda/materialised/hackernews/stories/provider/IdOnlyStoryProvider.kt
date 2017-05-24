package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section

interface IdOnlyStoryProvider {
    fun idOnlyStoriesFor(section: Section, callback: ValueCallback<List<Story>>)
}
