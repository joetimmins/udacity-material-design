package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.IdOnlyStoryProvider
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.StoryViewData
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class StorySectionPresenter(
        val idOnlyStoryProvider: IdOnlyStoryProvider,
        val storyProvider: StoryProvider,
        val storiesView: AsyncListView<StoryViewData>,
        val navigator: Navigator,
        val observeScheduler: Scheduler,
        val subscribeScheduler: Scheduler
) : Presenter<Section> {

    private var idOnlyStories: List<Story> = emptyList()

    override fun present(section: Section) {
        idOnlyStoryProvider.idOnlyStoriesFor(section)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(this::showErrorOnEmpty, { storiesView.showError() })

        storiesView.updateWith(idOnlyStories.map(this::convertStoryToStoryViewModel))

        val idList = idOnlyStories.map { story -> story.id }
        storyProvider.readItems(idList)
                .subscribeOn(Schedulers.io())
                .observeOn(observeScheduler)
                .subscribe({
                    story ->
                    val storyViewModel = convertStoryToStoryViewModel(story)
                    storiesView.updateWith(storyViewModel)
                })

    }

    private fun showErrorOnEmpty(idOnlyStoryList: List<Story>): Unit {
        when {
            idOnlyStoryList.isEmpty() -> storiesView.showError()
            else -> idOnlyStories = idOnlyStoryList
        }
    }

    private fun convertStoryToStoryViewModel(story: Story): ViewModel<StoryViewData> {
        val viewBehaviour = buildViewBehaviour(story)
        val storyViewData = StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
        return ViewModel(storyViewData, viewBehaviour)
    }

    private fun buildViewBehaviour(story: Story): (StoryViewData) -> Unit {
        when {
            Story.isIdOnly(story) -> return {}
            else -> return { storyViewData -> navigator.navigateTo(storyViewData.url) }
        }
    }
}
