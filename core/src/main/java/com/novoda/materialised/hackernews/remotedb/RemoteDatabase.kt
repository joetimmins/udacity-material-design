package com.novoda.materialised.hackernews.remotedb


interface RemoteDatabase {
    fun node(name: String): RemoteDatabaseNode
}
