package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.generics.AsyncListView
import com.novoda.materialised.hackernews.generics.ValueCallback
import com.novoda.materialised.hackernews.generics.valueCallbackFor
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase
import com.novoda.materialised.hackernews.topstories.database.Story
import com.novoda.materialised.hackernews.topstories.database.TopStoriesDatabase
import com.novoda.materialised.hackernews.topstories.view.StoryViewModelClickListener

class TopStoriesPresenter(
        val topStoriesDatabase: TopStoriesDatabase,
        val itemsDatabase: ItemsDatabase,
        val topStoriesView: AsyncListView<StoryViewModel>,
        val navigator: Navigator
) {
    fun present() {
        topStoriesDatabase.readAll(callbackWithAllStoriesInList(topStoriesView))
    }

    private fun callbackWithAllStoriesInList(topStoriesView: AsyncListView<StoryViewModel>): ValueCallback<List<Long>> {
        return valueCallbackFor {
            if (it.size > 0) {
                topStoriesView.updateWith(createIdOnlyViewModels(it))
                itemsDatabase.readItems(convertLongsToInts(it), callbackToStoriesViewWithSingleStoryViewModel(topStoriesView))
            }
        }
    }

    private fun createIdOnlyViewModels(listOfLongs: List<Long>): List<StoryViewModel> {
        return listOfLongs.map { createIdOnlyViewModel(it) }
    }

    private fun createIdOnlyViewModel(storyId: Long): StoryViewModel {
        return StoryViewModel().copy(id = storyId.toInt())
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map { it.toInt() }

    private fun callbackToStoriesViewWithSingleStoryViewModel(topStoriesView: AsyncListView<StoryViewModel>): ValueCallback<Story> {
        return valueCallbackFor {
            topStoriesView.updateWith(convertStoryToViewModel(it, StoryViewModelClickListener(navigator)))
        }
    }
}
