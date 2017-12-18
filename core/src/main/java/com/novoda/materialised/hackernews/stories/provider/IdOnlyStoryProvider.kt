package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Observable
import io.reactivex.Single

class IdOnlyStoryProvider(
        private val remoteDatabase: RemoteDatabase
) {

    fun idOnlyStoriesFor(section: Section): Single<List<Story>> {
        return remoteDatabase.node("v0").child(section.id).singleListOf(Long::class.java)
                .flatMapObservable { longs -> Observable.fromIterable(longs) }
                .map { rawId -> Story(id = rawId.toInt()) }
                .reduce(listOf(), { stories, story ->
                    stories.plusElement(story)
                })
    }
}
