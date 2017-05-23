package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Single

interface StoryIdProvider {
    fun listOfStoryIds(section: Section): Single<List<Long>>
}
