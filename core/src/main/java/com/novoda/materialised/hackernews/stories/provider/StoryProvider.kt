package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

internal class StoryProvider(
        private val storySingleProvider: StorySingleProvider
) {

    fun readItems(ids: List<Int>): Observable<Story> {
        return Observable.fromIterable(ids)
                .concatMap { id -> storySingleProvider.obtainStory(id).toObservable() }
    }
}
