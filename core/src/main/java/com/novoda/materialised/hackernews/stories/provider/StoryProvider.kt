package com.novoda.materialised.hackernews.stories.provider

interface StoryProvider {
    fun readItems(ids: List<Int>, valueCallback: ValueCallback<Story>)
}
