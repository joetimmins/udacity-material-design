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
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class StorySectionPresenter(
    private val storyIdProvider: StoryIdProvider,
    private val storyProvider: StoryProvider,
    private val storiesView: AsyncListView<StoryViewData>,
    private val navigator: Navigator,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : Presenter<Section> {

    private lateinit var storiesDisposable: Disposable

    override fun present(section: Section) {
        val storyIds: Single<List<Int>> = storyIdProvider.storyIdsFor(section)
            .map { list -> list.map { it.toInt() } }

        val first: Observable<ViewModel<StoryViewData>> = storyIds
            .doOnSuccess { if (it.isEmpty()) storiesView.showError(Throwable()) }
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toViewModel() }

        val second: Observable<ViewModel<StoryViewData>> = storyIds
            .flatMapObservable { storyProvider.readItems(it) }
            .map { it.toViewModel() }

        storiesDisposable = Observable.concat(first, second)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribe({ storiesView.updateWith(it) }, { storiesView.showError(it) })
    }

    private fun Int.toViewModel(): ViewModel<StoryViewData> = ViewModel(viewData = StoryViewData.JustAnId(id = this))

    private fun Story.toViewModel(): ViewModel<StoryViewData> = ViewModel(
        viewBehaviour = { navigator.navigateTo(url) },
        viewData = StoryViewData.FullyPopulated(by, kids, id, score, title, url) as StoryViewData
    )

    override fun stop() {
        if (!storiesDisposable.isDisposed) storiesDisposable.dispose()
    }
}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     storyProvider: StoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<StoryViewData>) -> Presenter<Section> =
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
