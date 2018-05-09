package com.novoda.materialised.hackernews.remotedb

import io.reactivex.Single

interface RemoteDatabaseNode {
    fun child(id: String): RemoteDatabaseNode
    fun <T> singleValueOf(returnClass: Class<T>): Single<T>
    fun <T> singleListOf(returnClass: Class<T>): Single<List<T>>
}
