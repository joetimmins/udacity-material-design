package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.NoOpClickListener
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.stories.database.*
import com.novoda.materialised.hackernews.stories.view.StoryClickListener
import com.novoda.materialised.hackernews.stories.view.StoryViewData
import com.novoda.materialised.hackernews.stories.view.StoryViewModel

internal class StoriesPresenter(
        val storyIdDatabase: StoryIdDatabase,
        val itemsDatabase: ItemsDatabase,
        val storiesView: AsyncListView<StoryViewModel>,
        val navigator: Navigator
) : TypedPresenter<String> {
    override fun present(type: String) {
        storyIdDatabase.readStoryIds(type, callbackWithAllStoriesInList(storiesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: AsyncListView<StoryViewModel>): ValueCallback<List<Long>> {
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

    private fun createIdOnlyViewModels(listOfIdInts: List<Int>): List<StoryViewModel> {
        return listOfIdInts.map { storyId -> createIdOnlyViewModel(storyId) }
    }

    private fun createIdOnlyViewModel(storyId: Int): StoryViewModel {
        return StoryViewModel(StoryViewData(id = storyId), NoOpClickListener)
    }

    private fun viewUpdaterFor(storiesView: AsyncListView<StoryViewModel>): ValueCallback<Story> {
        return valueCallbackOf {
            story ->
            val storyViewModel = convertStoryToStoryViewModel(story)
            storiesView.updateWith(storyViewModel)
        }
    }

    private fun convertStoryToStoryViewModel(story: Story): StoryViewModel {
        val storyViewData = StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
        return StoryViewModel(storyViewData, StoryClickListener(navigator))
    }
}
