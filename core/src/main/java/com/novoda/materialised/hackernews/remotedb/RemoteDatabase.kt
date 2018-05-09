package com.novoda.materialised.hackernews.remotedb


interface RemoteDatabase {
    fun child(name: String): RemoteDatabaseNode
}
