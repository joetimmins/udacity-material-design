package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.stories.provider.Story;
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider;
import com.novoda.materialised.hackernews.stories.provider.SingleStoryProvider;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.fest.assertions.api.Assertions.assertThat;

public class StorySectionPresenterTest {

    private static final int TEST_TIME = 3471394;
    private static final long FIRST_STORY_ID = 56L;
    private static final long SECOND_STORY_ID = 78L;
    private static final List<Long> ID_ONLY_STORIES = Arrays.asList(
            FIRST_STORY_ID,
            SECOND_STORY_ID
    );

    private static final Story A_STORY = new Story("test author", 123, (int) FIRST_STORY_ID, Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url");
    private static final Story ANOTHER_STORY = new Story("another author", 456, (int) SECOND_STORY_ID, Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url");

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewData_WhenPresentingMultipleStories() {
        StoryViewData firstIdOnlyViewData = buildIdOnlyViewData((int) FIRST_STORY_ID);
        StoryViewData secondIdOnlyViewData = buildIdOnlyViewData((int) SECOND_STORY_ID);
        List<StoryViewData> expectedViewData = Arrays.asList(firstIdOnlyViewData, secondIdOnlyViewData);

        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(ID_ONLY_STORIES, Collections.<Story>emptyList(), storiesView, new SpyingNavigator());

        assertThat(storiesView.receivedData).isEqualTo(expectedViewData);
    }

    @Test
    public void presenterTellsViewToShowErrorScreen_WhenNoIdOnlyStoriesAreRetrieved() {
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(Collections.<Long>emptyList(), Collections.<Story>emptyList(), storiesView, new SpyingNavigator());

        assertThat(storiesView.errorShown).isTrue();
    }

    @Test
    public void presenterGivesViewModelsWithFullViewDataToView_OneAtATime_WhenPresentingMultipleStories() {
        StoryViewData expectedViewData = createStoryViewDataFrom(A_STORY);
        StoryViewData anotherExpectedViewData = createStoryViewDataFrom(ANOTHER_STORY);
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(ID_ONLY_STORIES, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, new SpyingNavigator());

        StoryViewData actualViewData = storiesView.firstUpdatedViewModel.getViewData();
        StoryViewData anotherActualViewData = storiesView.secondUpdatedViewModel.getViewData();

        assertThat(actualViewData).isEqualTo(expectedViewData);
        assertThat(anotherActualViewData).isEqualTo(anotherExpectedViewData);
    }

    @Test
    public void presenterGivesViewModelWithNavigatingClickListenerToView_WhenPresenting() {
        SpyingStoriesView storiesView = new SpyingStoriesView();
        SpyingNavigator navigator = new SpyingNavigator();

        presentWith(ID_ONLY_STORIES, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, navigator);

        storiesView.firstUpdatedViewModel.invokeBehaviour();

        assertThat(navigator.uri).isEqualTo(A_STORY.getUrl());
    }

    private StoryViewData createStoryViewDataFrom(Story story) {
        return new StoryViewData(
                story.getBy(), story.getKids(), story.getId(), story.getScore(), story.getTitle(), story.getUrl()
        );
    }

    private void presentWith(List<Long> storyIds, List<Story> stories, AsyncListView<StoryViewData> storiesView, Navigator navigator) {
        StorySectionPresenter presenter = new StorySectionPresenter(
                new StubbedStoryIdProvider(storyIds),
                new StubbedSingleStoryProvider(stories),
                storiesView,
                navigator,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );
        presenter.present(Section.NEW);
    }

    private StoryViewData buildIdOnlyViewData(int storyId) {
        StoryViewData empty = new StoryViewData();
        return new StoryViewData(
                empty.getBy(), empty.getCommentIds(), storyId, empty.getScore(), empty.getTitle(), empty.getUrl()
        );
    }

    private static class StubbedStoryIdProvider implements StoryIdProvider {
        private final List<Long> ids;

        private StubbedStoryIdProvider(List<Long> ids) {
            this.ids = ids;
        }

        @NotNull
        @Override
        public Single<List<Long>> listOfStoryIds(@NotNull Section section) {
            return Single.just(ids);
        }
    }

    private static class StubbedSingleStoryProvider implements SingleStoryProvider {
        private List<Story> stories;

        StubbedSingleStoryProvider(List<Story> stories) {
            this.stories = stories;
        }

        @NotNull
        @Override
        public Single<Story> obtainStory(int storyId) {
            for (Story story : stories) {
                if (story.getId() == storyId) {
                    return Single.just(story);
                }
            }
            return Single.never();
        }
    }

    private static class SpyingStoriesView implements AsyncListView<StoryViewData> {
        List<StoryViewData> receivedData = new ArrayList<>();
        ViewModel<StoryViewData> firstUpdatedViewModel;
        ViewModel<StoryViewData> secondUpdatedViewModel;
        boolean errorShown;

        @Override
        public void updateWith(@NotNull List<ViewModel<StoryViewData>> initialViewModelList) {
            for (ViewModel<StoryViewData> viewModel : initialViewModelList) {
                receivedData.add(viewModel.getViewData());
            }
        }

        @Override
        public void updateWith(@NotNull ViewModel<StoryViewData> viewModel) {
            if (firstUpdatedViewModel == null) {
                firstUpdatedViewModel = viewModel;
            } else if (secondUpdatedViewModel == null) {
                secondUpdatedViewModel = viewModel;
            }
        }

        @Override
        public void showError() {
            errorShown = true;
        }
    }

    private static class SpyingNavigator implements Navigator {
        String uri;

        @Override
        public void navigateTo(@NotNull String uri) {
            this.uri = uri;
        }
    }
}
