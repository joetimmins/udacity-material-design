package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.provider.SingleStoryProvider
import com.novoda.materialised.hackernews.stories.provider.Story
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider
import com.novoda.materialised.hackernews.stories.view.StoryViewData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class StorySectionPresenterTest {

    @Test
    fun presenterGivesCorrectListOfIdsToView_AsViewData_WhenPresentingMultipleStories() {
        val firstIdOnlyViewData = buildIdOnlyViewData(FIRST_STORY_ID.toInt())
        val secondIdOnlyViewData = buildIdOnlyViewData(SECOND_STORY_ID.toInt())
        val expectedViewData = Arrays.asList(firstIdOnlyViewData, secondIdOnlyViewData)

        val storiesView = SpyingStoriesView()

        presentWith(ID_ONLY_STORIES, emptyList(), storiesView, SpyingNavigator())

        assertThat(storiesView.receivedData).isEqualTo(expectedViewData)
    }

    @Test
    fun presenterTellsViewToShowErrorScreen_WhenNoIdOnlyStoriesAreRetrieved() {
        val storiesView = SpyingStoriesView()

        presentWith(emptyList(), emptyList(), storiesView, SpyingNavigator())

        assertThat(storiesView.errorShown).isTrue
    }

    @Test
    fun presenterGivesViewModelsWithFullViewDataToView_OneAtATime_WhenPresentingMultipleStories() {
        val expectedViewData = createStoryViewDataFrom(A_STORY)
        val anotherExpectedViewData = createStoryViewDataFrom(ANOTHER_STORY)
        val storiesView = SpyingStoriesView()

        presentWith(ID_ONLY_STORIES, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, SpyingNavigator())

        val actualViewData = storiesView.firstUpdatedViewModel!!.viewData
        val anotherActualViewData = storiesView.secondUpdatedViewModel!!.viewData

        assertThat(actualViewData).isEqualTo(expectedViewData)
        assertThat(anotherActualViewData).isEqualTo(anotherExpectedViewData)
    }

    @Test
    fun presenterGivesViewModelWithNavigatingClickListenerToView_WhenPresenting() {
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

    private fun presentWith(storyIds: List<Long>, stories: List<Story>, storiesView: AsyncListView<StoryViewData>, navigator: Navigator) {
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

    private fun buildIdOnlyViewData(storyId: Int): StoryViewData = StoryViewData(id = storyId)

    private class StubbedStoryIdProvider(private val ids: List<Long>) : StoryIdProvider {
        override fun listOfStoryIds(section: Section): Single<List<Long>> = Single.just(ids)
    }

    private class StubbedSingleStoryProvider(private val stories: List<Story>) : SingleStoryProvider {

        override fun obtainStory(storyId: Int): Single<Story> {
            return stories
                    .firstOrNull { it.id == storyId }
                    ?.let { Single.just(it) }
                    ?: Single.never()
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
        private val FIRST_STORY_ID = 56L
        private val SECOND_STORY_ID = 78L
        private val ID_ONLY_STORIES = Arrays.asList(
                FIRST_STORY_ID,
                SECOND_STORY_ID
        )

        private val A_STORY = Story("test author", 123, FIRST_STORY_ID.toInt(), Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url")
        private val ANOTHER_STORY = Story("another author", 456, SECOND_STORY_ID.toInt(), Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url")
    }
}
