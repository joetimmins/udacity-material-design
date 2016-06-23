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
            itemsDatabase.readItem(it[0].toInt(), callbackToStoryView(storyView))
        }
    }

    private fun callbackToStoryView(storyView: StoryView): ValueCallback<StoryViewModel> {
        return valueCallbackFor {
            storyView.updateWith(it)
        }
    }

    private fun callbackToStoriesViewWithSingleStoryViewModel(storyView: StoriesView): ValueCallback<StoryViewModel> {
        return valueCallbackFor {
            storyView.updateWith(it)
        }
    }

    fun presentMultipleStoriesWith(storiesView: StoriesView) {
        topStoriesDatabase.readAll(callbackWithAllStoriesInList(storiesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: StoriesView): ValueCallback<List<Long>> {
        return valueCallbackFor {
            storiesView.updateWith(createIdOnlyViewModels(it))
//            itemsDatabase.readItems(convertLongsToInts(it), callbackWithListOfStories(storiesView))
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

    private fun callbackWithListOfStories(storiesView: StoriesView): ValueCallback<List<StoryViewModel>> {
        return valueCallbackFor {
            storiesView.updateWith(it)
        }
    }
}
