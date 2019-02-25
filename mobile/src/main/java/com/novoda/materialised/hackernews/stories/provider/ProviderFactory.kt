package com.novoda.materialised.hackernews.stories.provider

import android.content.Context

import com.novoda.remotedb.Metadata
import com.novoda.remotedb.Node
import com.novoda.remotedb.NodeProvider
import com.novoda.remotedb.Structure

import io.reactivex.Single

object ProviderFactory {

    private const val APPLICATION_ID = "com.novoda.materialised"
    private const val APPLICATION_NAME = "Material HN"
    private const val DATABASE_URL = "https://hacker-news.firebaseio.com"
    private val METADATA = Metadata(APPLICATION_ID, APPLICATION_NAME, DATABASE_URL)

    fun newStoryProvider(context: Context): StoryProvider {
        val nodeProvider = NodeProvider.obtain(context, METADATA)
        val structure = Structure("v0", "item")
        val node = nodeProvider.node(structure)
        return StoryProvider(from(node))
    }

    fun newStoryIdProvider(context: Context): StoryIdProvider {
        val nodeProvider = NodeProvider.obtain(context, METADATA)
        val structure = Structure("v0")
        val node = nodeProvider.node(structure)
        return StoryIdProvider(from(node))
    }

    private fun from(node: Node): RemoteDatabaseNode {
        return object : RemoteDatabaseNode {
            override fun child(id: String): RemoteDatabaseNode {
                return from(node.child(id))
            }

            override fun <T> singleValueOf(returnClass: Class<T>): Single<T> {
                return node.singleValueOf(returnClass)
            }

            override fun <T> singleListOf(returnClass: Class<T>): Single<List<T>> {
                return node.singleListOf(returnClass)
            }
        }
    }
}
