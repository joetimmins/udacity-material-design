package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Observable
import io.reactivex.Single

internal class IdOnlyStoryProvider(
        private val storyIdProvider: StoryIdProvider
) {

    fun idOnlyStoriesFor(section: Section): Single<List<Story>> {
        val listOfStoryIds = storyIdProvider.listOfStoryIds(section)

        return listOfStoryIds
                .flatMapObservable { longs -> Observable.fromIterable(longs) }
                .map { rawId -> Story(id = rawId.toInt()) }
                .reduce(listOf(), { stories, story ->
                    stories.plusElement(story)
                })
    }
}
