package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Single

interface StorySinglesProvider {
    fun obtainStory(storyId: Int): Single<Story>
}
