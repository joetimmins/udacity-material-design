package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.UiModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.UiStory
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class StorySectionPresenter(
    private val storyIdProvider: StoryIdProvider,
    private val storyProvider: StoryProvider,
    private val storiesView: AsyncListView<UiStory>,
    private val navigator: Navigator,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : Presenter<Section> {

    private lateinit var storiesDisposable: Disposable

    override fun present(section: Section) {
        val storyIds: Single<List<Int>> = storyIdProvider.storyIdsFor(section)
            .map { list -> list.map { it.toInt() } }

        val first: Observable<UiModel<UiStory>> = storyIds
            .doOnSuccess { if (it.isEmpty()) storiesView.showError(Throwable()) }
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toUiState() }

        val second: Observable<UiModel<UiStory>> = storyIds
            .flatMapObservable { storyProvider.readItems(it) }
            .map { it.toUiState() }

        storiesDisposable = Observable.concat(first, second)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribe({ storiesView.updateWith(it) }, { storiesView.showError(it) })
    }

    private fun Int.toUiState(): UiModel<UiStory> = UiModel(data = UiStory(id = this))

    private fun Story.toUiState(): UiModel<UiStory> = UiModel(
        behaviour = { navigator.navigateTo(url) },
        data = UiStory(by, kids, id, score, title, url)
    )

    override fun stop() {
        if (!storiesDisposable.isDisposed) storiesDisposable.dispose()
    }
}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     storyProvider: StoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<UiStory>) -> Presenter<Section> =
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
