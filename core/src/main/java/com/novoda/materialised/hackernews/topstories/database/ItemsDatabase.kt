package com.novoda.materialised.hackernews.topstories.database

interface ItemsDatabase {
    fun readItems(ids: List<Int>, valueCallback: ValueCallback<Story>)
}
