package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.*
import com.novoda.materialised.hackernews.stories.view.StoryViewData

class StorySectionPresenter(
        val idOnlyStoryProvider: IdOnlyStoryProvider,
        val storyProvider: StoryProvider,
        val storiesView: AsyncListView<StoryViewData>,
        val navigator: Navigator
) : Presenter<Section> {
    override fun present(section: Section) {
        idOnlyStoryProvider.idOnlyStoriesFor(section, callbackWithAllStoriesInList(storiesView))
    }

    private fun callbackWithAllStoriesInList(storiesView: AsyncListView<StoryViewData>): ValueCallback<List<Story>> {
        return valueCallbackOf {
            idOnlyStories ->
            if (idOnlyStories.isNotEmpty()) {
                val idOnlyViewModels = idOnlyStories.map { idOnlyStory -> convertStoryToStoryViewModel(idOnlyStory) }
                storiesView.updateWith(idOnlyViewModels)
                val viewUpdater = viewUpdaterFor(storiesView)
                storyProvider.readItems(ids, viewUpdater)
            } else {
                storiesView.showError()
            }
        }
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
