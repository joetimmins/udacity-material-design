package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Single

interface StoryObservableProvider {
    fun obtainStories(storyIds: List<Int>): List<Single<Story>>
}
