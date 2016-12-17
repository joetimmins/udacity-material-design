package com.novoda.materialised.hackernews.stories.database

interface ItemsDatabase {
    fun readItems(ids: List<Int>, valueCallback: ValueCallback<Story>)
}
