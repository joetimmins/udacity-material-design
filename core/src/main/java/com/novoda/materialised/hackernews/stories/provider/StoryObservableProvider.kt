package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

interface StoryObservableProvider {
    fun createStoryObservables(storyIds: List<Int>): List<Observable<Story>>
}
