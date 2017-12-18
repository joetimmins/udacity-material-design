package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Single


interface RemoteDatabaseNode {
    fun child(nodeId: String): RemoteDatabaseNode
    fun <T> singleValue(): Single<T>
    fun <T> singleList(): Single<List<T>>
}
