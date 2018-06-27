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

class StorySectionPresenter constructor(
        private val storyIdProvider: StoryIdProvider,
        private val storyProvider: StoryProvider,
        private val storiesView: AsyncListView<StoryViewData>,
        private val navigator: Navigator,
        private val subscribeScheduler: Scheduler,
        private val observeScheduler: Scheduler
) : Presenter<Section> {

    override fun present(section: Section) {
        val storyIdsFor = storyIdProvider.storyIdsFor(section)

        val first = storyIdsFor
                .doOnSuccess { if (it.isEmpty()) storiesView.showError(Throwable()) }
                .map { storyIds -> storyIds.map { ViewModel(viewData = StoryViewData.JustAnId(id = it.toInt())) } }
                .flatMapObservable { Observable.fromIterable(it) }

        val second: Observable<ViewModel<StoryViewData>> = storyIdsFor
                .flatMapObservable { storyIds -> storyProvider.readItems(storyIds.map { it.toInt() }) }
                .map { toViewModel(it) }

        Observable.concat(first, second)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe({ storiesView.updateWith(it as ViewModel<StoryViewData>) }, { storiesView.showError(it) })
    }

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
