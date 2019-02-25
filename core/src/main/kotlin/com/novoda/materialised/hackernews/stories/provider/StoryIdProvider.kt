package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Single

class StoryIdProvider(
    private val sectionDatabase: RemoteDatabaseNode
) {
    fun storyIdsFor(section: Section): Single<List<Long>> = sectionDatabase.child(section.id).singleListOf(Long::class.java)
}
