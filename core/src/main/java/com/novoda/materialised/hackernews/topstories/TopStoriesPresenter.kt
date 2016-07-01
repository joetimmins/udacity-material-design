package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.ClickListener
import com.novoda.materialised.hackernews.ValueCallback
import com.novoda.materialised.hackernews.items.ItemsDatabase
import com.novoda.materialised.hackernews.items.StoryViewModel
import com.novoda.materialised.hackernews.valueCallbackFor

class TopStoriesPresenter(
        val topStoriesDatabase: TopStoriesDatabase,
        val itemsDatabase: ItemsDatabase,
        val topStoriesView: TopStoriesView,
        val clickListener: ClickListener<StoryViewModel>
) {

    fun present() {
        topStoriesDatabase.readAll(callbackWithAllStoriesInList(topStoriesView))
    }

    private fun callbackWithAllStoriesInList(topStoriesView: TopStoriesView): ValueCallback<List<Long>> {
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
        val emptyStoryViewModel = StoryViewModel();
        return emptyStoryViewModel.copy(id = storyId.toInt())
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map { it.toInt() }

    private fun callbackToStoriesViewWithSingleStoryViewModel(topStoriesView: TopStoriesView): ValueCallback<StoryViewModel> {
        return valueCallbackFor {
            topStoriesView.updateWith(it, clickListener)
        }
    }
}
