package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

internal class StoryProvider(
        private val storyObservableProvider: StoryObservableProvider
) {

    fun readItems(ids: List<Int>): Observable<Story> {
        val storyObservables = storyObservableProvider.createStoryObservables(ids)
        return Observable.fromIterable(storyObservables)
                .reduce { storyObservable, nextStoryObservable -> storyObservable.mergeWith(nextStoryObservable) }
                .flatMapObservable { storyObservable -> storyObservable }
    }
}
