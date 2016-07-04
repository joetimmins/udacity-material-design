package com.novoda.materialised.hackernews.topstories.database

import com.novoda.materialised.hackernews.generics.ValueCallback
import com.novoda.materialised.hackernews.topstories.database.Story

interface ItemsDatabase {
    fun readItem(id: Int, valueCallback: com.novoda.materialised.hackernews.generics.ValueCallback<Story>)

    fun readItems(ids: List<Int>, valueCallback: com.novoda.materialised.hackernews.generics.ValueCallback<Story>)
}
