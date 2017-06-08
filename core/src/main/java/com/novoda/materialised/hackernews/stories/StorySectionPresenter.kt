package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.*
import com.novoda.materialised.hackernews.stories.view.StoryViewData
import io.reactivex.Scheduler

class StorySectionPresenter private constructor(
        val idOnlyStoryProvider: IdOnlyStoryProvider,
        val storyProvider: StoryProvider,
        val storiesView: AsyncListView<StoryViewData>,
        val navigator: Navigator,
        val subscribeScheduler: Scheduler,
        val observeScheduler: Scheduler
) : Presenter<Section> {

    constructor(
            storyIdProvider: StoryIdProvider,
            storyObservableProvider: StoryObservableProvider,
            storiesView: AsyncListView<StoryViewData>,
            navigator: Navigator,
            subscribeScheduler: Scheduler,
            observeScheduler: Scheduler
    ) : this(
            IdOnlyStoryProvider(storyIdProvider),
            StoryProvider(storyObservableProvider),
            storiesView,
            navigator,
            subscribeScheduler,
            observeScheduler
    )

    override fun present(section: Section) {
        idOnlyStoryProvider.idOnlyStoriesFor(section)
                .map(convertAllStories())
                .doAfterSuccess(updateStoriesView())
                .map(extractStoryIds())
                .flatMapObservable { idList -> storyProvider.readItems(idList) }
                .map { story -> convertStoryToStoryViewModel(story) }
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe({ storyViewModel -> storiesView.updateWith(storyViewModel) }, { storiesView.showError() })
    }

    private fun extractStoryIds(): (List<ViewModel<StoryViewData>>) -> List<Int> {
        return { storyViewModels -> storyViewModels.map { (viewData) -> viewData.id } }
    }

    private fun convertAllStories(): (List<Story>) -> List<ViewModel<StoryViewData>> {
        return { stories -> stories.map { story -> convertStoryToStoryViewModel(story) } }
    }

    private fun updateStoriesView(): (List<ViewModel<StoryViewData>>) -> Unit {
        return { storyViewModels ->
            when {
                storyViewModels.isEmpty() -> storiesView.showError()
                else -> storiesView.updateWith(storyViewModels)
            }
        }
    }

    private fun convertStoryToStoryViewModel(story: Story): ViewModel<StoryViewData> {
        return ViewModel(
                StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url),
                buildViewBehaviour(story)
        )
    }

    private fun buildViewBehaviour(story: Story): (StoryViewData) -> Unit {
        when {
            Story.isIdOnly(story) -> return {}
            else -> return { storyViewData -> navigator.navigateTo(storyViewData.url) }
        }
    }
}
