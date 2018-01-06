package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.StoryViewData
import io.reactivex.Scheduler

class StorySectionPresenter constructor(
        private val storyIdProvider: StoryIdProvider,
        private val storyProvider: StoryProvider,
        private val storiesView: AsyncListView<StoryViewData>,
        private val navigator: Navigator,
        private val subscribeScheduler: Scheduler,
        private val observeScheduler: Scheduler
) : Presenter<Section> {

    override fun present(section: Section) {
        storyIdProvider.storyIdsFor(section)
                .map { stories: List<Long> -> stories.map(createBlankViewModel) }
                .doAfterSuccess(updateStoriesView)
                .map(extractStoryIds)
                .flatMapObservable { idList -> storyProvider.readItems(idList) }
                .map(mapStoryToViewModel)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(onNext, onError)
    }

    private val createBlankViewModel: (Long) -> ViewModel<StoryViewData> = { storyId -> ViewModel(StoryViewData(id = storyId.toInt())) }

    private val mapStoryToViewModel: (Story) -> ViewModel<StoryViewData> = { story ->
        ViewModel(
                mapStoryToViewData(story),
                { storyViewData -> navigator.navigateTo(storyViewData.url) }
        )
    }

    private fun mapStoryToViewData(story: Story): StoryViewData = StoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)

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
                     storyProvider: StoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<StoryViewData>) -> Presenter<Section> {
    return { asyncListView ->
        StorySectionPresenter(
                storyIdProvider,
                storyProvider,
                asyncListView,
                navigator,
                subscribeScheduler,
                observeScheduler
        )
    }
}
