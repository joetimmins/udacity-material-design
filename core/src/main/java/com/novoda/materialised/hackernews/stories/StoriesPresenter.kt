package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.TypedPresenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.database.*
import com.novoda.materialised.hackernews.stories.view.StoryViewData

class StoriesPresenter(
        val storyIdProvider: StoryIdProvider,
        val storyProvider: StoryProvider,
        val storiesView: AsyncListView<StoryViewData>,
        val navigator: Navigator
) : TypedPresenter<Section> {
    override fun present(type: Section) {
        storyIdProvider.readStoryIds(type, callbackWithAllStoriesInList(storiesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: AsyncListView<StoryViewData>): ValueCallback<List<Long>> {
        return valueCallbackOf {
            idList ->
            if (idList.isNotEmpty()) {
                val ids = convertLongsToInts(idList)
                val idOnlyViewModels = createIdOnlyViewModels(ids)
                storiesView.updateWith(idOnlyViewModels)
                val viewUpdater = viewUpdaterFor(storiesView)
                storyProvider.readItems(ids, viewUpdater)
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
        return ViewModel(StoryViewData(id = storyId))
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
        return ViewModel(storyViewData, { storyViewData -> navigator.navigateTo(storyViewData.url) })
    }
}
