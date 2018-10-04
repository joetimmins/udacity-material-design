package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.RemoteDatabaseNode
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.StoryViewData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class StorySectionPresenterTest {

    @Test
    fun `When presenting multiple stories, the presenter gives a list of ids to the view, wrapped in view data objects`() {
        val firstBlankViewData = StoryViewData.JustAnId(id = FIRST_STORY_ID.toInt())
        val secondBlankViewData = StoryViewData.JustAnId(id = SECOND_STORY_ID.toInt())
        val expectedViewData: List<StoryViewData> = listOf(firstBlankViewData, secondBlankViewData)
        val expectedViewModels = expectedViewData.map { ViewModel(viewData = it) }

        val storiesView = SpyingStoriesView()

        presentWith(STORY_IDS, emptyList(), storiesView, SpyingNavigator())

        assertThat(storiesView.receivedViewModels).isEqualTo(expectedViewModels)
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

        val receivedViewData: List<StoryViewData> = storiesView.receivedViewModels
                .asSequence()
                .filter { it.viewData is StoryViewData.FullyPopulated }
                .map { it.viewData }
                .toList()

        assertThat(receivedViewData).containsExactly(expectedViewData, moreExpectedViewData)
    }

    @Test
    fun `When a view model's behaviour is invoked, the navigator is given the url from that view model to navigate to`() {
        val storiesView = SpyingStoriesView()
        val navigator = SpyingNavigator()

        presentWith(STORY_IDS, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, navigator)

        storiesView.receivedViewModels[2].invokeBehaviour()

        assertThat(navigator.uri).isEqualTo(A_STORY.url)
    }

    private fun createStoryViewDataFrom(story: Story): StoryViewData.FullyPopulated {
        return StoryViewData.FullyPopulated(
                story.by, story.kids, story.id, story.score, story.title, story.url
        )
    }

    private fun presentWith(storyIds: List<Long>, stories: List<Story>, storiesView: AsyncListView<StoryViewData>, navigator: Navigator) {
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

    private class SpyingStoriesView : AsyncListView<StoryViewData> {
        internal var errorShown: Boolean = false
        internal val receivedErrors: MutableList<Throwable> = mutableListOf()
        internal val receivedViewModels: MutableList<ViewModel<StoryViewData>> = mutableListOf()

        override fun updateWith(viewModel: ViewModel<StoryViewData>) {
            receivedViewModels.add(viewModel)
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
