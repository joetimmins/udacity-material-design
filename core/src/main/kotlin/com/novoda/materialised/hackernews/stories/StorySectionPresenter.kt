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

class StorySectionPresenter constructor(
        private val storyIdProvider: StoryIdProvider,
        private val storyProvider: StoryProvider,
        private val storiesView: AsyncListView<StoryViewData>,
        private val navigator: Navigator,
        private val subscribeScheduler: Scheduler,
        private val observeScheduler: Scheduler
) : Presenter<Section> {

    override fun present(section: Section) {
        val storyIds: Single<List<Int>> = storyIdProvider.storyIdsFor(section)
                .map { list -> list.map { it.toInt() } }

        val first: Observable<ViewModel<StoryViewData>> = storyIds
                .doOnSuccess { if (it.isEmpty()) storiesView.showError(Throwable()) }
                .map { ids: List<Int> -> ids.map { toViewModel(it) } }
                .flatMapObservable { Observable.fromIterable(it) }

        val second: Observable<ViewModel<StoryViewData>> = storyIds
                .flatMapObservable { ids -> storyProvider.readItems(ids.map { it }) }
                .map { toViewModel(it) }

        Observable.concat(first, second)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe({ storiesView.updateWith(it) }, { storiesView.showError(it) })
    }

    private fun toViewModel(id: Int): ViewModel<StoryViewData> = ViewModel(viewData = StoryViewData.JustAnId(id = id))

    private fun toViewModel(story: Story): ViewModel<StoryViewData> = ViewModel(
            viewBehaviour = { navigator.navigateTo(story.url) },
            viewData = StoryViewData.FullyPopulated(story.by, story.kids, story.id, story.score, story.title, story.url) as StoryViewData
    )

}

fun partialPresenter(storyIdProvider: StoryIdProvider,
                     storyProvider: StoryProvider,
                     navigator: Navigator,
                     subscribeScheduler: Scheduler,
                     observeScheduler: Scheduler): (AsyncListView<StoryViewData>) -> Presenter<Section> = { asyncListView ->
    StorySectionPresenter(
            storyIdProvider,
            storyProvider,
            asyncListView,
            navigator,
            subscribeScheduler,
            observeScheduler
    )
}
