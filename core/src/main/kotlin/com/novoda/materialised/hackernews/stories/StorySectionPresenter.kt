package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.UiState
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.StoryUiData
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class StorySectionPresenter(
    private val storyIdProvider: StoryIdProvider,
    private val storyProvider: StoryProvider,
    private val storiesView: AsyncListView<StoryUiData>,
    private val navigator: Navigator,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : Presenter<Section> {

    private lateinit var storiesDisposable: Disposable

    override fun present(section: Section) {
        val storyIds: Single<List<Int>> = storyIdProvider.storyIdsFor(section)
            .map { list -> list.map { it.toInt() } }

        val first: Observable<UiState<StoryUiData>> = storyIds
            .doOnSuccess { if (it.isEmpty()) storiesView.showError(Throwable()) }
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toViewModel() }

        val second: Observable<UiState<StoryUiData>> = storyIds
            .flatMapObservable { storyProvider.readItems(it) }
            .map { it.toViewModel() }

        storiesDisposable = Observable.concat(first, second)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribe({ storiesView.updateWith(it) }, { storiesView.showError(it) })
    }

    private fun Int.toViewModel(): UiState<StoryUiData> = UiState(viewData = StoryUiData.JustAnId(id = this))

    private fun Story.toViewModel(): UiState<StoryUiData> = UiState(
        viewBehaviour = { navigator.navigateTo(url) },
        viewData = StoryUiData.FullyPopulated(by, kids, id, score, title, url) as StoryUiData
    )

    override fun stop() {
        if (!storiesDisposable.isDisposed) storiesDisposable.dispose()
    }
}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     storyProvider: StoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<StoryUiData>) -> Presenter<Section> =
    { asyncListView ->
        StorySectionPresenter(
            storyIdProvider,
            storyProvider,
            asyncListView,
            navigator,
            subscribeScheduler,
            observeScheduler
        )
    }
