package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.generics.*
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase
import com.novoda.materialised.hackernews.topstories.database.Story
import com.novoda.materialised.hackernews.topstories.database.TopStoriesDatabase
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel

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
        val dataWithIdOnly = com.novoda.materialised.hackernews.topstories.view.StoryViewData().copy(id = storyId.toInt())
        return com.novoda.materialised.hackernews.topstories.view.StoryViewModel(dataWithIdOnly, NoOpClickListener())
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map { it.toInt() }

    private fun callbackToStoriesViewWithSingleStoryViewModel(topStoriesView: AsyncListView<StoryViewModel>): ValueCallback<Story> {
        return valueCallbackFor {
            topStoriesView.updateWith(convert(it))
        }
    }

    private fun convert(story: Story): StoryViewModel {
        val storyViewData = com.novoda.materialised.hackernews.topstories.view.StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
        return com.novoda.materialised.hackernews.topstories.view.StoryViewModel(storyViewData, object : ClickListener<com.novoda.materialised.hackernews.topstories.view.StoryViewData> {
            override fun onClick(data: com.novoda.materialised.hackernews.topstories.view.StoryViewData) {
                navigator.navigateTo(data.url)
            }
        })
    }
}
