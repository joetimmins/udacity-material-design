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
            singleStoryProvider: SingleStoryProvider,
            storiesView: AsyncListView<StoryViewData>,
            navigator: Navigator,
            subscribeScheduler: Scheduler,
            observeScheduler: Scheduler
    ) : this(
            IdOnlyStoryProvider(storyIdProvider),
            StoryProvider(singleStoryProvider),
            storiesView,
            navigator,
            subscribeScheduler,
            observeScheduler
    )

    override fun present(section: Section) {
        idOnlyStoryProvider.idOnlyStoriesFor(section)
                .map { stories -> stories.map(mapStoryToIdOnlyViewModel) }
                .doAfterSuccess(updateStoriesView)
                .map(extractStoryIds)
                .flatMapObservable { idList -> storyProvider.readItems(idList) }
                .map(mapStoryToViewModel)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(onNext, onError)
    }

    private val mapStoryToIdOnlyViewModel: (Story) -> ViewModel<StoryViewData> = { story -> ViewModel(mapStoryToViewData(story)) }

    private val mapStoryToViewModel: (Story) -> ViewModel<StoryViewData> = { story ->
        ViewModel(
                mapStoryToViewData(story),
                { storyViewData -> navigator.navigateTo(storyViewData.url) }
        )
    }

    private fun mapStoryToViewData(story: Story) = StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)

    private val extractStoryIds: (List<ViewModel<StoryViewData>>) -> List<Int> = { storyViewModels ->
        storyViewModels.map { (viewData) -> viewData.id }
    }

    private val updateStoriesView: (List<ViewModel<StoryViewData>>) -> Unit = { storyViewModels ->
        when {
            storyViewModels.isEmpty() -> storiesView.showError()
            else -> storiesView.updateWith(storyViewModels)
        }
    }

    private val onNext: (ViewModel<StoryViewData>) -> Unit = { storyViewModel -> storiesView.updateWith(storyViewModel) }

    private val onError: (Throwable) -> Unit = { storiesView.showError() }
}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     singleStoryProvider: SingleStoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<StoryViewData>) -> Presenter<Section> {
    return { asyncListView ->
        StorySectionPresenter(
                storyIdProvider,
                singleStoryProvider,
                asyncListView,
                navigator,
                subscribeScheduler,
                observeScheduler
        )
    }
}
