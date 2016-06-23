package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.ValueCallback
import com.novoda.materialised.hackernews.items.ItemsDatabase
import com.novoda.materialised.hackernews.items.StoryViewModel
import com.novoda.materialised.hackernews.valueCallbackFor

class TopStoriesPresenter(val topStoriesDatabase: TopStoriesDatabase, val itemsDatabase: ItemsDatabase) {

    fun presentMultipleStoriesWith(storiesView: StoriesView) {
        topStoriesDatabase.readAll(callbackWithAllStoriesInList(storiesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: StoriesView): ValueCallback<List<Long>> {
        return valueCallbackFor {
            storiesView.updateWith(createIdOnlyViewModels(it))
            itemsDatabase.readItems(convertLongsToInts(it), callbackToStoriesViewWithSingleStoryViewModel(storiesView))
        }
    }

    private fun createIdOnlyViewModels(listOfLongs: List<Long>): List<StoryViewModel> {
        return listOfLongs.map { createIdOnlyViewModel(it) }
    }

    private fun createIdOnlyViewModel(storyId: Long): StoryViewModel {
        val emptyStoryViewModel = StoryViewModel();
        return emptyStoryViewModel.copy(id = storyId.toInt())
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map { it.toInt() }

    private fun callbackToStoriesViewWithSingleStoryViewModel(storiesView: StoriesView): ValueCallback<StoryViewModel> {
        return valueCallbackFor {
            storiesView.updateWith(it)
        }
    }
}
