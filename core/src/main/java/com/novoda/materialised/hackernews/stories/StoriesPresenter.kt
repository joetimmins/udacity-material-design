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
                val idOnlyViewModels = createIdOnlyViewModels(idList)
                storiesView.updateWith(idOnlyViewModels)
                val ids = convertLongsToInts(idList)
                val viewUpdater = viewUpdaterFor(storiesView)
                itemsDatabase.readItems(ids, viewUpdater)
            } else {
                storiesView.showError()
            }
        }
    }

    private fun createIdOnlyViewModels(listOfLongs: List<Long>): List<StoryViewModel> {
        return listOfLongs.map { storyId -> createIdOnlyViewModel(storyId) }
    }

    private fun createIdOnlyViewModel(storyId: Long): StoryViewModel {
        val dataWithIdOnly = StoryViewData().copy(id = storyId.toInt())
        return StoryViewModel(dataWithIdOnly, NoOpClickListener.INSTANCE)
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map(Long::toInt)

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
