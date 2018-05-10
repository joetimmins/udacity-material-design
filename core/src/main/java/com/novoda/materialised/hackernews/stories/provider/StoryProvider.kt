package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNode
import io.reactivex.Observable

class StoryProvider(
        private val itemsDatabase: RemoteDatabaseNode
) {

    fun readItems(ids: List<Int>): Observable<Story> {
        return Observable.fromIterable(ids)
                .flatMap { id ->
                    itemsDatabase.child(id.toString()).singleValueOf(Story::class.java).toObservable()
                }
    }
}
