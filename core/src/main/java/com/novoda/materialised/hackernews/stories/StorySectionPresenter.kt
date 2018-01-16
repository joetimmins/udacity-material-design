package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.FullStoryViewData
import io.reactivex.Scheduler

class StorySectionPresenter constructor(
        private val storyIdProvider: StoryIdProvider,
        private val storyProvider: StoryProvider,
        private val storiesView: AsyncListView<FullStoryViewData>,
        private val navigator: Navigator,
        private val subscribeScheduler: Scheduler,
        private val observeScheduler: Scheduler
) : Presenter<Section> {

    override fun present(section: Section) {
        storyIdProvider.storyIdsFor(section)
                .map { stories: List<Long> -> stories.map({ storyId -> ViewModel(FullStoryViewData(id = storyId.toInt())) }) }
                .doAfterSuccess(updateStoriesView)
                .map(extractStoryIds)
                .flatMapObservable { idList -> storyProvider.readItems(idList) }
                .map(mapStoryToViewModel)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(updateViewOnNext, showErrorOnError)
    }

    private val mapStoryToViewModel: (Story) -> ViewModel<FullStoryViewData> = { story ->
        ViewModel(
                FullStoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url),
                { storyViewData -> navigator.navigateTo(storyViewData.url) }
        )
    }

    private val extractStoryIds: (List<ViewModel<FullStoryViewData>>) -> List<Int> = { storyViewModels ->
        storyViewModels.map { (viewData) -> viewData.id }
    }

    private val updateStoriesView: (List<ViewModel<FullStoryViewData>>) -> Unit = { storyViewModels ->
        if (storyViewModels.isEmpty()) storiesView.showError()
        else storiesView.updateWith(storyViewModels)
    }

    private val updateViewOnNext: (ViewModel<FullStoryViewData>) -> Unit = { storyViewModel -> storiesView.updateWith(storyViewModel) }

    private val showErrorOnError: (Throwable) -> Unit = { storiesView.showError() }
}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     storyProvider: StoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler
): (AsyncListView<FullStoryViewData>) -> Presenter<Section> = { asyncListView ->
    StorySectionPresenter(
            storyIdProvider,
            storyProvider,
            asyncListView,
            navigator,
            subscribeScheduler,
            observeScheduler
    )
}
