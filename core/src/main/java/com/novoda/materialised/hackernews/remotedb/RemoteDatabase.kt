package com.novoda.materialised.hackernews.remotedb


interface RemoteDatabase {
    fun child(childId: String): RemoteDatabaseNode
}
