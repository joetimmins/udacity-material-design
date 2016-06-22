package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.ValueCallback
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
        topStoriesDatabase.readAll(valueCallbackForReadingStoryIds(storiesView))
    }

    private fun valueCallbackForReadingStoryIds(storiesView: StoriesView): ValueCallback<List<Long>> {
        return valueCallbackFor {
            storiesView.updateWith(it.size)
            itemsDatabase.readItems(convertLongsToInts(it), valueCallbackForReadingEveryItem(storiesView))
        }
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map { it.toInt() }

    private fun valueCallbackForReadingEveryItem(storiesView: StoriesView): ValueCallback<List<StoryViewModel>> {
        return valueCallbackFor {
            storiesView.updateWith(it)
        }
    }
}
