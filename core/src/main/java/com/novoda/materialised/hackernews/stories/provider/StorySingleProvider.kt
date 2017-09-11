package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Single

interface StorySingleProvider {
    fun obtainStory(storyId: Int): Single<Story>
}
