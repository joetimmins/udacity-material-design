package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.remotedb.RemoteDatabase
import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNode
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.provider.StoryProvider
import com.novoda.materialised.hackernews.stories.view.FullStoryViewData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class StorySectionPresenterTest {

    @Test
    fun `When presenting multiple stories, the presenter gives a list of ids to the view, wrapped in view data objects`() {
        val firstBlankViewData = FullStoryViewData(id = FIRST_STORY_ID.toInt())
        val secondBlankViewData = FullStoryViewData(id = SECOND_STORY_ID.toInt())
        val expectedViewData = Arrays.asList(firstBlankViewData, secondBlankViewData)

        val storiesView = SpyingStoriesView()

        presentWith(STORY_IDS, emptyList(), storiesView, SpyingNavigator())

        assertThat(storiesView.receivedData).isEqualTo(expectedViewData)
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

        val actualViewData = storiesView.firstUpdatedViewModel.viewData
        val moreActualViewData = storiesView.secondUpdatedViewModel.viewData

        assertThat(actualViewData).isEqualTo(expectedViewData)
        assertThat(moreActualViewData).isEqualTo(moreExpectedViewData)
    }

    @Test
    fun `When a view model's behaviour is invoked, the navigator is given the url from that view model to navigate to`() {
        val storiesView = SpyingStoriesView()
        val navigator = SpyingNavigator()

        presentWith(STORY_IDS, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, navigator)

        storiesView.firstUpdatedViewModel.invokeBehaviour()

        assertThat(navigator.uri).isEqualTo(A_STORY.url)
    }

    private fun createStoryViewDataFrom(story: Story): FullStoryViewData {
        return FullStoryViewData(
                story.by, story.kids, story.id, story.score, story.title, story.url
        )
    }

    private fun presentWith(storyIds: List<Long>, stories: List<Story>, storiesView: AsyncListView<FullStoryViewData>, navigator: Navigator) {
        val remoteDatabase = FakeStoriesDatabase(storyIds, stories)
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

    class FakeStoriesDatabase(listReturnValue: List<Any>, singleReturnValues: List<Any>) : RemoteDatabase {

        private val theOnlyNode: Node = Node(listReturnValue, singleReturnValues)

        override fun child(childId: String): RemoteDatabaseNode = theOnlyNode
    }

    private class Node(private val listReturnValue: List<Any>, private val singleReturnValues: List<Any>) : RemoteDatabaseNode {

        override fun child(childId: String): RemoteDatabaseNode = this

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

    private class SpyingStoriesView : AsyncListView<FullStoryViewData> {
        internal lateinit var receivedData: List<FullStoryViewData>
        internal lateinit var firstUpdatedViewModel: ViewModel<FullStoryViewData>
        internal lateinit var secondUpdatedViewModel: ViewModel<FullStoryViewData>
        internal var errorShown: Boolean = false

        override fun updateWith(initialViewModelList: List<ViewModel<FullStoryViewData>>) {
            receivedData = initialViewModelList.map { viewModel -> viewModel.viewData }
        }

        override fun updateWith(viewModel: ViewModel<FullStoryViewData>) {
            when {
                !::firstUpdatedViewModel.isInitialized -> firstUpdatedViewModel = viewModel
                !::secondUpdatedViewModel.isInitialized -> secondUpdatedViewModel = viewModel
            }
        }

        override fun showError() {
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

        private val TEST_TIME = 3471394
        private val FIRST_STORY_ID = 56L
        private val SECOND_STORY_ID = 78L
        private val STORY_IDS = listOf(
                FIRST_STORY_ID,
                SECOND_STORY_ID
        )

        private val A_STORY = Story("test author", 123, FIRST_STORY_ID.toInt(), Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url")
        private val ANOTHER_STORY = Story("another author", 456, SECOND_STORY_ID.toInt(), Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url")
    }
}
