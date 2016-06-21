package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.items.ItemsDatabase
import com.novoda.materialised.hackernews.items.StoryViewModel
import com.novoda.materialised.hackernews.valueCallbackFor

class TopStoriesPresenter(val topStoriesDatabase: TopStoriesDatabase, val itemsDatabase: ItemsDatabase, val storyView: StoryView) {
    fun startPresenting() {
        topStoriesDatabase.readAll(
                valueCallbackFor<List<Long>> {
                    itemsDatabase.readItem(
                            it[0].toInt(),
                            valueCallbackFor<StoryViewModel> {
                                storyView.updateWith(it)
                            }
                    )
                }
        )
    }
}
