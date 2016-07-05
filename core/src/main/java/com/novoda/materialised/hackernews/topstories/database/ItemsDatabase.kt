package com.novoda.materialised.hackernews.topstories.database

import com.novoda.materialised.hackernews.topstories.database.ValueCallback
import com.novoda.materialised.hackernews.topstories.database.Story

interface ItemsDatabase {
    fun readItem(id: Int, valueCallback: ValueCallback<Story>)

    fun readItems(ids: List<Int>, valueCallback: ValueCallback<Story>)
}
