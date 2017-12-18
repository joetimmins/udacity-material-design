package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

class StoryProvider(
        private val remoteDatabase: RemoteDatabase
) {

    fun readItems(ids: List<Int>): Observable<Story> {
        return Observable.fromIterable(ids)
                .flatMap { id ->
                    remoteDatabase.node("v0").child("item").child(id.toString()).singleValueOf(Story::class.java).toObservable()
                }

    }
}
