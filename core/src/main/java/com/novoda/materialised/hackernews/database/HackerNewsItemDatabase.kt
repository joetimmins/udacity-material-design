package com.novoda.materialised.hackernews.database

import com.novoda.materialised.hackernews.StoryViewModel

interface HackerNewsItemDatabase {
    fun readItem(id: Int, valueCallback: ValueCallback<StoryViewModel>)

    interface ValueCallback<T> {
        fun onValueRetrieved(value: T)
    }
}
