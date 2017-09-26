package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

internal class StoryProvider(
        private val singleStoryProvider: SingleStoryProvider
) {

    fun readItems(ids: List<Int>): Observable<Story> {
        return Observable.fromIterable(ids)
                .flatMap { id -> singleStoryProvider.obtainStory(id).toObservable() }
    }
}
