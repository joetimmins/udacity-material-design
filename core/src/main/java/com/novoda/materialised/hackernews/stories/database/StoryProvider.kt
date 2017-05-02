package com.novoda.materialised.hackernews.stories.database

interface StoryProvider {
    fun readItems(ids: List<Int>, valueCallback: ValueCallback<Story>)
}
