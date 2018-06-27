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
        // TODO: find out how to do multiple subscribers
        storyIdProvider.storyIdsFor(section)
                .map { storyIds: List<Long> -> storyIds.map { storyId -> ViewModel(viewData = FullStoryViewData(id = storyId.toInt())) } }
                .doAfterSuccess { updateView(it) }
                .map(extractStoryIds)
                .flatMapObservable { idList -> storyProvider.readItems(idList) }
                .map { toViewModel(it) }
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(updateViewOnNext, showErrorOnError)
    }

    private fun updateView(viewModelList: List<ViewModel<FullStoryViewData>>) = when {
        viewModelList.isEmpty() -> storiesView.showError()
        else -> storiesView.updateWith(viewModelList)
    }

    private fun toViewModel(story: Story): ViewModel<FullStoryViewData> {
        return ViewModel(
                { navigator.navigateTo(story.url) },
                FullStoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
        )
    }

    private val extractStoryIds: (List<ViewModel<FullStoryViewData>>) -> List<Int> = { storyViewModels ->
        storyViewModels.map { it.viewData.id }
    }

    private val updateViewOnNext: (ViewModel<FullStoryViewData>) -> Unit = { storyViewModel -> storiesView.updateWith(storyViewModel) }

    private val showErrorOnError: (Throwable) -> Unit = { storiesView.showError() }
}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     storyProvider: StoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<FullStoryViewData>) -> Presenter<Section> = { asyncListView ->
    StorySectionPresenter(
            storyIdProvider,
            storyProvider,
            asyncListView,
            navigator,
            subscribeScheduler,
            observeScheduler
    )
}
