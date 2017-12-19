package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.RemoteDatabase
import com.novoda.materialised.hackernews.stories.provider.RemoteDatabaseNode
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.view.StoryViewData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class StorySectionPresenterTest {

    @Test
    fun `When presenting multiple stories, the presenter gives a list of ids to the view, wrapped in view data objects`() {
        val firstIdOnlyViewData = StoryViewData(id = FIRST_STORY_ID)
        val secondIdOnlyViewData = StoryViewData(id = SECOND_STORY_ID)
        val expectedViewData = Arrays.asList(firstIdOnlyViewData, secondIdOnlyViewData)

        val storiesView = SpyingStoriesView()

        presentWith(ID_ONLY_STORIES, emptyList(), storiesView, SpyingNavigator())

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

        presentWith(ID_ONLY_STORIES, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, SpyingNavigator())

        val actualViewData = storiesView.firstUpdatedViewModel!!.viewData
        val moreActualViewData = storiesView.secondUpdatedViewModel!!.viewData

        assertThat(actualViewData).isEqualTo(expectedViewData)
        assertThat(moreActualViewData).isEqualTo(moreExpectedViewData)
    }

    @Test
    fun `When a view model's behaviour is invoked, the navigator is given the url from that view model to navigate to`() {
        val storiesView = SpyingStoriesView()
        val navigator = SpyingNavigator()

        presentWith(ID_ONLY_STORIES, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, navigator)

        storiesView.firstUpdatedViewModel!!.invokeBehaviour()

        assertThat(navigator.uri).isEqualTo(A_STORY.url)
    }

    private fun createStoryViewDataFrom(story: Story): StoryViewData {
        return StoryViewData(
                story.by, story.kids, story.id, story.score, story.title, story.url
        )
    }

    private fun presentWith(storyIds: List<Int>, stories: List<Story>, storiesView: AsyncListView<StoryViewData>, navigator: Navigator) {
        val presenter = StorySectionPresenter(
                StubbedStoryIdProvider(storyIds),
                StubbedSingleStoryProvider(stories),
                storiesView,
                navigator,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        )
        presenter.present(Section.NEW)
    }

    private class FakeStoriesDatabase : RemoteDatabase {
        override fun node(name: String): RemoteDatabaseNode = CanIEvenDoThis()
    }

    private class CanIEvenDoThis : RemoteDatabaseNode {
        override fun child(nodeId: String): RemoteDatabaseNode = this

        override fun <T> singleValueOf(returnClass: Class<T>): Single<T> {

        }

        override fun <T> singleListOf(returnClass: Class<T>): Single<List<T>> {
        }

    }

    private class SpyingStoriesView : AsyncListView<StoryViewData> {
        internal var receivedData: MutableList<StoryViewData> = ArrayList()
        internal var firstUpdatedViewModel: ViewModel<StoryViewData>? = null
        internal var secondUpdatedViewModel: ViewModel<StoryViewData>? = null
        internal var errorShown: Boolean = false

        override fun updateWith(initialViewModelList: List<ViewModel<StoryViewData>>) {
            for ((viewData) in initialViewModelList) {
                receivedData.add(viewData)
            }
        }

        override fun updateWith(viewModel: ViewModel<StoryViewData>) {
            if (firstUpdatedViewModel == null) {
                firstUpdatedViewModel = viewModel
            } else if (secondUpdatedViewModel == null) {
                secondUpdatedViewModel = viewModel
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
        private val FIRST_STORY_ID = 56
        private val SECOND_STORY_ID = 78
        private val ID_ONLY_STORIES = Arrays.asList(
                FIRST_STORY_ID,
                SECOND_STORY_ID
        )

        private val A_STORY = Story("test author", 123, FIRST_STORY_ID, Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url")
        private val ANOTHER_STORY = Story("another author", 456, SECOND_STORY_ID, Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url")
    }
}
