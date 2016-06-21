package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.items.ItemsDatabase
import com.novoda.materialised.hackernews.items.StoryViewModel
import com.novoda.materialised.hackernews.valueCallbackFor

class TopStoriesPresenter(val topStoriesDatabase: TopStoriesDatabase, val itemsDatabase: ItemsDatabase) {
    fun presentSingleStoryWith(storyView: StoryView) {
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

    fun presentMultipleStoriesWith(storiesView: StoriesView) {
        topStoriesDatabase.readAll(
                valueCallbackFor<List<Long>> {
                    itemsDatabase.readItems(
                            it.map { it.toInt() },
                            valueCallbackFor<List<StoryViewModel>> {
                                storiesView.updateWith(it)
                            }
                    )
                }
        )
    }
}
