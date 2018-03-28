package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.remotedb.RemoteDatabase
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
