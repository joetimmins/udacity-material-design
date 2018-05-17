package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNode
import io.reactivex.Observable

class StoryProvider(
        private val itemsDatabase: RemoteDatabaseNode
) {

    fun readItems(ids: List<Int>): Observable<Story> {
        // TODO filter out items that don't have type == STORY to clean up the logcat output if nothing else
        // or maybe not, that would need another class to deserialise to
        return Observable.fromIterable(ids)
                .flatMap { id ->
                    itemsDatabase.child(id.toString()).singleValueOf(Story::class.java).toObservable()
                }
    }
}
