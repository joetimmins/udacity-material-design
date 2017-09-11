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
        private val idOnlyStoryProvider: IdOnlyStoryProvider,
        private val storyProvider: StoryProvider,
        private val storiesView: AsyncListView<StoryViewData>,
        private val navigator: Navigator,
        private val subscribeScheduler: Scheduler,
        private val observeScheduler: Scheduler
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
                .map { stories -> stories.map(mapStoryToViewModel) }
                .doAfterSuccess(updateStoriesView)
                .map(extractStoryIds)
                .flatMapObservable { idList -> storyProvider.readItems(idList) }
                .map(mapStoryToViewModel)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(onNext, onError)
    }

    private val mapStoryToViewModel: (Story) -> ViewModel<StoryViewData> = {
        story ->
        ViewModel(
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

    private val extractStoryIds: (List<ViewModel<StoryViewData>>) -> List<Int> = {
        storyViewModels ->
        storyViewModels.map { (viewData) -> viewData.id }
    }

    private val updateStoriesView: (List<ViewModel<StoryViewData>>) -> Unit = {
        storyViewModels ->
        when {
            storyViewModels.isEmpty() -> storiesView.showError()
            else -> storiesView.updateWith(storyViewModels)
        }
    }

    private val onNext: (ViewModel<StoryViewData>) -> Unit = { storyViewModel -> storiesView.updateWith(storyViewModel) }

    private val onError: (Throwable) -> Unit = { storiesView.showError() }
}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     storyObservableProvider: StoryObservableProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<StoryViewData>) -> Presenter<Section> {
    return { asyncListView ->
        StorySectionPresenter(
                storyIdProvider,
                storyObservableProvider,
                asyncListView,
                navigator,
                subscribeScheduler,
                observeScheduler
        )
    }
}
