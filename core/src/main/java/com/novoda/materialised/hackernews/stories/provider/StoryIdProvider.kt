package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.remotedb.RemoteDatabase
import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Single

class StoryIdProvider(
        private val sectionDatabase: RemoteDatabase
) {
    fun storyIdsFor(section: Section): Single<List<Long>> = sectionDatabase.child("v0").child(section.id).singleListOf(Long::class.java)
}
