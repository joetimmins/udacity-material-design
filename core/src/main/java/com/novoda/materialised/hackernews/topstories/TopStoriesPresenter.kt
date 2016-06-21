package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.ValueCallback
import com.novoda.materialised.hackernews.items.ItemsDatabase
import com.novoda.materialised.hackernews.items.StoryViewModel

class TopStoriesPresenter(val topStoriesDatabase: TopStoriesDatabase, val itemsDatabase: ItemsDatabase, val storyView: StoryView) {
    fun startPresenting() {
        topStoriesDatabase.readAll(
                object : ValueCallback<List<Long>> {
                    override fun onValueRetrieved(value: List<Long>) {
                        itemsDatabase.readItem(value[0].toInt(), object : ValueCallback<StoryViewModel> {
                            override fun onValueRetrieved(value: StoryViewModel) {
                                storyView.updateWith(value)
                            }
                        })
                    }
                })
    }
}
