package com.novoda.materialised.hackernews.items

import com.novoda.materialised.hackernews.ValueCallback

interface ItemsDatabase {
    fun readItem(id: Int, valueCallback: ValueCallback<Story>)

    fun readItems(ids: List<Int>, valueCallback: ValueCallback<Story>)
}
