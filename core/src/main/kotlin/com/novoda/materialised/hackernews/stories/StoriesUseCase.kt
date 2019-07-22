package com.novoda.materialised.hackernews.stories

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

class StoriesUseCase(
    private val storyIdProvider: StoryIdProvider,
    private val storyProvider: StoryProvider,
    private val navigator: Navigator,
    private val subscribeScheduler: Scheduler
) {
    fun stories(section: Section): Observable<UiModel<UiStory>> {
        val storyIds: Single<List<Int>> = storyIdProvider.storyIdsFor(section)
            .map { list -> list.map { it.toInt() } }

        val first: Observable<UiModel<UiStory>> = storyIds
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toUiState() }

        val second: Observable<UiModel<UiStory>> = storyIds
            .flatMapObservable { storyProvider.readItems(it) }
            .map { it.toUiState() }

        return Observable.concat(first, second)
            .subscribeOn(subscribeScheduler)
    }

    private fun Int.toUiState(): UiModel<UiStory> = UiModel(data = UiStory(id = this))

    private fun Story.toUiState(): UiModel<UiStory> = UiModel(
        behaviour = { navigator.navigateTo(url) },
        data = UiStory(by, kids, id, score, title, url)
    )
}
