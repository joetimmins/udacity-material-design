package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.ValueCallback
import com.novoda.materialised.hackernews.items.ItemsDatabase
import com.novoda.materialised.hackernews.items.StoryViewModel
import com.novoda.materialised.hackernews.valueCallbackFor

class TopStoriesPresenter(val topStoriesDatabase: TopStoriesDatabase, val itemsDatabase: ItemsDatabase) {
    fun presentSingleStoryWith(storyView: StoryView) {
        topStoriesDatabase.readAll(callbackWithFirstStoryInList(storyView))
    }

    private fun callbackWithFirstStoryInList(storyView: StoryView): ValueCallback<List<Long>> {
        return valueCallbackFor {
            itemsDatabase.readItem(it[0].toInt(), callbackWithSingleStoryViewModel(storyView))
        }
    }

    private fun callbackWithSingleStoryViewModel(storyView: StoryView): ValueCallback<StoryViewModel> {
        return valueCallbackFor {
            storyView.updateWith(it)
        }
    }

    fun presentMultipleStoriesWith(storiesView: StoriesView) {
        topStoriesDatabase.readAll(callbackWithAllStoriesInList(storiesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: StoriesView): ValueCallback<List<Long>> {
        return valueCallbackFor {
            storiesView.updateWith(it.size)
            itemsDatabase.readItems(convertLongsToInts(it), callbackWithListOfStories(storiesView))
        }
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map { it.toInt() }

    private fun callbackWithListOfStories(storiesView: StoriesView): ValueCallback<List<StoryViewModel>> {
        return valueCallbackFor {
            storiesView.updateWith(it)
        }
    }
}
