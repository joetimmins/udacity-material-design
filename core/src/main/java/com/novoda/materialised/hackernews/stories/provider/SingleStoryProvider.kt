package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Single

interface SingleStoryProvider {
    fun obtainStory(storyId: Int): Single<Story>
}
