package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

internal class StoryProvider(
        private val storySinglesProvider: StorySinglesProvider
) {

    fun readItems(ids: List<Int>): Observable<Story> {
        val storyObservables = storySinglesProvider.obtainStories(ids)
        return Observable.fromIterable(storyObservables)
                .map { storySingle -> storySingle.toObservable() }
                .reduce { storyObservable, nextStoryObservable -> storyObservable.mergeWith(nextStoryObservable) }
                .flatMapObservable { storyObservable -> storyObservable }
    }
}
