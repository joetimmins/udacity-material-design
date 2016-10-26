package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.NoOpClickListener
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.topstories.database.*
import com.novoda.materialised.hackernews.topstories.view.StoryClickListener
import com.novoda.materialised.hackernews.topstories.view.StoryViewData
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel

class StoriesPresenter(
        val storyIdDatabase: StoryIdDatabase,
        val itemsDatabase: ItemsDatabase,
        val topStoriesView: AsyncListView<StoryViewModel>,
        val navigator: Navigator
) : TypedPresenter<String> {
    override fun present(type: String) {
        storyIdDatabase.readStoryIds(type, callbackWithAllStoriesInList(topStoriesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: AsyncListView<StoryViewModel>): ValueCallback<List<Long>> {
        return valueCallbackFor { idList ->
            if (idList.size > 0) {
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
        return StoryViewModel(dataWithIdOnly, NoOpClickListener())
    }

    private fun convertLongsToInts(listOfLongs: List<Long>) = listOfLongs.map(Long::toInt)

    private fun viewUpdaterFor(storiesView: AsyncListView<StoryViewModel>): ValueCallback<Story> {
        return valueCallbackFor { story ->
            val storyViewModel = convertStoryToStoryViewModel(story)
            storiesView.updateWith(storyViewModel)
        }
    }

    private fun convertStoryToStoryViewModel(story: Story): StoryViewModel {
        val storyViewData = StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
        return StoryViewModel(storyViewData, StoryClickListener(navigator))
    }
}
