package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.UiState
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.RemoteDatabaseNode
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.StoryUiData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class StorySectionPresenterTest {

    @Test
    fun `When presenting multiple stories, the presenter gives a list of ids to the view, wrapped in view data objects`() {
        val firstBlankViewData = StoryUiData.JustAnId(id = FIRST_STORY_ID.toInt())
        val secondBlankViewData = StoryUiData.JustAnId(id = SECOND_STORY_ID.toInt())
        val expectedViewData: List<StoryUiData> = listOf(firstBlankViewData, secondBlankViewData)
        val expectedViewModels = expectedViewData.map { UiState(data = it) }

        val storiesView = SpyingStoriesView()

        presentWith(STORY_IDS, emptyList(), storiesView, SpyingNavigator())

        assertThat(storiesView.receivedUiStates).isEqualTo(expectedViewModels)
    }

    @Test
    fun `When no id-only view data are retrieved, the presenter tells the view to show the error screen`() {
        val storiesView = SpyingStoriesView()

        presentWith(emptyList(), emptyList(), storiesView, SpyingNavigator())

        assertThat(storiesView.errorShown).isTrue
    }

    @Test
    fun `When presenting multiple stories, the presenter gives view models containing full view data to the view, one at a time`() {
        val expectedViewData = createStoryViewDataFrom(A_STORY)
        val moreExpectedViewData = createStoryViewDataFrom(ANOTHER_STORY)
        val storiesView = SpyingStoriesView()

        presentWith(STORY_IDS, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, SpyingNavigator())

        val receivedViewData: List<StoryUiData> = storiesView.receivedUiStates
                .asSequence()
                .filter { it.data is StoryUiData.FullyPopulated }
                .map { it.data }
                .toList()

        assertThat(receivedViewData).containsExactly(expectedViewData, moreExpectedViewData)
    }

    @Test
    fun `When a view model's behaviour is invoked, the navigator is given the url from that view model to navigate to`() {
        val storiesView = SpyingStoriesView()
        val navigator = SpyingNavigator()

        presentWith(STORY_IDS, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, navigator)

        storiesView.receivedUiStates[2].invokeBehaviour()

        assertThat(navigator.uri).isEqualTo(A_STORY.url)
    }

    private fun createStoryViewDataFrom(story: Story): StoryUiData.FullyPopulated {
        return StoryUiData.FullyPopulated(
                story.by, story.kids, story.id, story.score, story.title, story.url
        )
    }

    private fun presentWith(storyIds: List<Long>, stories: List<Story>, storiesView: AsyncListView<StoryUiData>, navigator: Navigator) {
        val remoteDatabase = Node(storyIds, stories)
        val presenter = StorySectionPresenter(
                StoryIdProvider(remoteDatabase),
                StoryProvider(remoteDatabase),
                storiesView,
                navigator,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        )
        presenter.present(Section.NEW)
    }

    private class Node(private val listReturnValue: List<Any>, private val singleReturnValues: List<Any>) : RemoteDatabaseNode {

        override fun child(id: String): RemoteDatabaseNode = this

        private var methodCount: Int = 0

        override fun <T> singleValueOf(returnClass: Class<T>): Single<T> {
            val castReturnValue = singleReturnValues[methodCount] as T
            methodCount = methodCount.plus(1)
            return Single.just(castReturnValue)
        }

        override fun <T> singleListOf(returnClass: Class<T>): Single<List<T>> {
            val castReturnValue = listReturnValue as List<T>
            return Single.just(castReturnValue)
        }

    }

    private class SpyingStoriesView : AsyncListView<StoryUiData> {
        internal var errorShown: Boolean = false
        internal val receivedErrors: MutableList<Throwable> = mutableListOf()
        internal val receivedUiStates: MutableList<UiState<StoryUiData>> = mutableListOf()

        override fun updateWith(uiState: UiState<StoryUiData>) {
            receivedUiStates.add(uiState)
        }

        override fun showError(throwable: Throwable) {
            receivedErrors.add(throwable)
            errorShown = true
        }
    }

    private class SpyingNavigator : Navigator {
        internal lateinit var uri: String

        override fun navigateTo(uri: String) {
            this.uri = uri
        }
    }

    companion object {

        private const val TEST_TIME = 3471394
        private const val FIRST_STORY_ID = 56L
        private const val SECOND_STORY_ID = 78L
        private val STORY_IDS = listOf(
                FIRST_STORY_ID,
                SECOND_STORY_ID
        )

        private val A_STORY = Story("test author", 123, FIRST_STORY_ID.toInt(), Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url")
        private val ANOTHER_STORY = Story("another author", 456, SECOND_STORY_ID.toInt(), Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url")
    }
}
