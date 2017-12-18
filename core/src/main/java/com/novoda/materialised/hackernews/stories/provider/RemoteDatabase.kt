package com.novoda.materialised.hackernews.stories.provider


interface RemoteDatabase {
    fun node(name: String): RemoteDatabaseNode
}
