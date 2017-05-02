package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ClickListener
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.stories.database.*
import com.novoda.materialised.hackernews.stories.view.StoryViewData

internal class StoriesPresenter(
        val storyIdDatabase: StoryIdDatabase,
        val itemsDatabase: ItemsDatabase,
        val storiesView: AsyncListView<StoryViewData>,
        val navigator: Navigator
) : TypedPresenter<StoryType> {
    override fun present(type: StoryType) {
        storyIdDatabase.readStoryIds(type, callbackWithAllStoriesInList(storiesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: AsyncListView<StoryViewData>): ValueCallback<List<Long>> {
        return valueCallbackOf {
            idList ->
            if (idList.isNotEmpty()) {
                val ids = convertLongsToInts(idList)
                val idOnlyViewModels = createIdOnlyViewModels(ids)
                storiesView.updateWith(idOnlyViewModels)
                val viewUpdater = viewUpdaterFor(storiesView)
                itemsDatabase.readItems(ids, viewUpdater)
            } else {
                storiesView.showError()
            }
        }
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map(Long::toInt)

    private fun createIdOnlyViewModels(listOfIdInts: List<Int>): List<ViewModel<StoryViewData>> {
        return listOfIdInts.map { storyId -> createIdOnlyViewModel(storyId) }
    }

    private fun createIdOnlyViewModel(storyId: Int): ViewModel<StoryViewData> {
        return ViewModel(StoryViewData(id = storyId), ClickListener.noOpClickListener)
    }

    private fun viewUpdaterFor(storiesView: AsyncListView<StoryViewData>): ValueCallback<Story> {
        return valueCallbackOf {
            story ->
            val storyViewModel = convertStoryToStoryViewModel(story)
            storiesView.updateWith(storyViewModel)
        }
    }

    private fun convertStoryToStoryViewModel(story: Story): ViewModel<StoryViewData> {
        val storyViewData = StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
        return ViewModel(storyViewData, storyClickListener)
    }

    val storyClickListener: ClickListener<StoryViewData> = object : ClickListener<StoryViewData> {
        override fun onClick(data: StoryViewData) {
            navigator.navigateTo(data.url)
        }
    }
}
