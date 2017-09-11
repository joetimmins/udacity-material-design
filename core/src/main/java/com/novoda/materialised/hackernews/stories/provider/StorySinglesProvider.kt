package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Single

interface StorySinglesProvider {
    fun obtainStories(storyIds: List<Int>): List<Single<Story>>
}
