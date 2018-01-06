package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Single

class StoryIdProvider(
        private val remoteDatabase: RemoteDatabase
) {
    fun storyIdsFor(section: Section): Single<List<Long>> = remoteDatabase.node("v0").child(section.id).singleListOf(Long::class.java)
}
