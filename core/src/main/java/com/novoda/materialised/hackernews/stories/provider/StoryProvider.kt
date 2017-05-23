package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

class StoryProvider internal constructor(private val storyObservableProvider: StoryObservableProvider) {

    fun readItems(ids: List<Int>): Observable<Story> {
        return storyObservableProvider.createStoryObservables(ids)
                .reduce { storyObservable, nextStoryObservable -> storyObservable.mergeWith(nextStoryObservable) }
                .flatMapObservable { storyObservable -> storyObservable }
    }
}
