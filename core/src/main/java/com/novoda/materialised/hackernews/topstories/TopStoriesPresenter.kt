package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.generics.*
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase
import com.novoda.materialised.hackernews.topstories.database.Story
import com.novoda.materialised.hackernews.topstories.database.TopStoriesDatabase

class TopStoriesPresenter(
        val topStoriesDatabase: TopStoriesDatabase,
        val itemsDatabase: ItemsDatabase,
        val topStoriesView: DecoupledAsyncListView<DecoupledStoryViewModel>,
        val navigator: Navigator
) {
    fun present() {
        topStoriesDatabase.readAll(callbackWithAllStoriesInList(topStoriesView))
    }

    private fun callbackWithAllStoriesInList(topStoriesView: DecoupledAsyncListView<DecoupledStoryViewModel>): ValueCallback<List<Long>> {
        return valueCallbackFor {
            if (it.size > 0) {
                topStoriesView.updateWith(createIdOnlyViewModels(it))
                itemsDatabase.readItems(convertLongsToInts(it), callbackToStoriesViewWithSingleStoryViewModel(topStoriesView))
            }
        }
    }

    private fun createIdOnlyViewModels(listOfLongs: List<Long>): List<DecoupledStoryViewModel> {
        return listOfLongs.map { createIdOnlyViewModel(it) }
    }

    private fun createIdOnlyViewModel(storyId: Long): DecoupledStoryViewModel {
        val dataWithIdOnly = StoryViewData().copy(id = storyId.toInt())
        return DecoupledStoryViewModel(dataWithIdOnly, NoOpClickListener())
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map { it.toInt() }

    private fun callbackToStoriesViewWithSingleStoryViewModel(topStoriesView: DecoupledAsyncListView<DecoupledStoryViewModel>): ValueCallback<Story> {
        return valueCallbackFor {
            topStoriesView.updateWith(convert(it))
        }
    }

    private fun convert(story: Story): DecoupledStoryViewModel {
        val storyViewData = StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
        return DecoupledStoryViewModel(storyViewData, object : ClickListener<StoryViewData> {
            override fun onClick(data: StoryViewData) {
                navigator.navigateTo(data.url)
            }
        })
    }
}
