package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Single

interface IdOnlyStoryProvider {
    fun idOnlyStoriesFor(section: Section): Single<List<Story>>
}
