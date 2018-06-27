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
import io.reactivex.Observable
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
        val storyIdsFor = storyIdProvider.storyIdsFor(section)

        val first = storyIdsFor
                .doOnSuccess { if (it.isEmpty()) storiesView.showError(Throwable()) }
                .map { storyIds -> storyIds.map { ViewModel(viewData = FullStoryViewData(id = it.toInt())) } }
                .flatMapObservable { Observable.fromIterable(it) }

        val second: Observable<ViewModel<FullStoryViewData>> = storyIdsFor
                .flatMapObservable { storyIds -> storyProvider.readItems(storyIds.map { it.toInt() }) }
                .map { toViewModel(it) }

        Observable.concat(first, second)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe({ storiesView.updateWith(it) }, { storiesView.showError(it) })
    }

    private fun toViewModel(story: Story) = ViewModel(
            viewBehaviour = { navigator.navigateTo(story.url) },
            viewData = FullStoryViewData(story.by, story.kids, story.id, story.score, story.title, story.url)
    )

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
