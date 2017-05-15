package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.stories.provider.StoryProvider;
import com.novoda.materialised.hackernews.stories.provider.Story;
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider;
import com.novoda.materialised.hackernews.stories.provider.ValueCallback;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StorySectionPresenterTest {

    private static final int TEST_TIME = 3471394;
    private static final long FIRST_STORY_ID = 56L;
    private static final long SECOND_STORY_ID = 78L;
    private static final List<Long> TOP_STORY_IDS = Arrays.asList(FIRST_STORY_ID, SECOND_STORY_ID);

    private static final Story A_STORY = new Story("test author", 123, (int) FIRST_STORY_ID, Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url");
    private static final Story ANOTHER_STORY = new Story("another author", 456, (int) SECOND_STORY_ID, Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url");

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewData_WhenPresentingMultipleStories() {
        StoryViewData firstIdOnlyViewData = buildIdOnlyViewData(FIRST_STORY_ID);
        StoryViewData secondIdOnlyViewData = buildIdOnlyViewData(SECOND_STORY_ID);
        List<StoryViewData> expectedViewData = Arrays.asList(firstIdOnlyViewData, secondIdOnlyViewData);

        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(TOP_STORY_IDS, Collections.<Story>emptyList(), storiesView, new SpyingNavigator());

        assertThat(storiesView.receivedData).isEqualTo(expectedViewData);
    }

    @Test
    public void presenterTellsViewToShowErrorScreen_WhenNoStoryIdsAreRetrieved() {
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(Collections.<Long>emptyList(), Collections.<Story>emptyList(), storiesView, new SpyingNavigator());

        assertThat(storiesView.errorShown).isTrue();
    }

    @Test
    public void presenterGivesViewModelsWithFullViewDataToView_OneAtATime_WhenPresentingMultipleStories() {
        StoryViewData expectedViewData = createStoryViewDataFrom(A_STORY);
        StoryViewData anotherExpectedViewData = createStoryViewDataFrom(ANOTHER_STORY);
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(TOP_STORY_IDS, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, new SpyingNavigator());

        StoryViewData actualViewData = storiesView.firstUpdatedViewModel.getViewData();
        StoryViewData anotherActualViewData = storiesView.secondUpdatedViewModel.getViewData();

        assertThat(actualViewData).isEqualTo(expectedViewData);
        assertThat(anotherActualViewData).isEqualTo(anotherExpectedViewData);
    }

    @Test
    public void presenterGivesViewModelWithNavigatingClickListenerToView_WhenPresenting() {
        SpyingStoriesView storiesView = new SpyingStoriesView();
        SpyingNavigator navigator = new SpyingNavigator();

        presentWith(TOP_STORY_IDS, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, navigator);

        storiesView.firstUpdatedViewModel.onClick();

        assertThat(navigator.uri).isEqualTo(A_STORY.getUrl());
    }

    private StoryViewData createStoryViewDataFrom(Story story) {
        return new StoryViewData(
                story.getBy(), story.getKids(), story.getId(), story.getScore(), story.getTitle(), story.getUrl()
        );
    }

    private void presentWith(List<Long> topStoryIds, List<Story> stories, AsyncListView<StoryViewData> storiesView, Navigator navigator) {
        StorySectionPresenter presenter = new StorySectionPresenter(
                new StubbedStoryIdProvider(topStoryIds),
                new StubbedStoryProvider(stories),
                storiesView,
                navigator
        );
        presenter.present(Section.NEW);
    }

    private StoryViewData buildIdOnlyViewData(long storyId) {
        StoryViewData empty = new StoryViewData();
        return new StoryViewData(
                empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl()
        );
    }

    private static class StubbedStoryIdProvider implements StoryIdProvider {
        final List<Long> ids;

        private StubbedStoryIdProvider(List<Long> ids) {
            this.ids = ids;
        }

        @Override
        public void readStoryIds(@NotNull Section section, @NotNull ValueCallback<? super List<Long>> callback) {
            callback.onValueRetrieved(ids);
        }
    }

    private static class StubbedStoryProvider implements StoryProvider {
        private List<Story> stories;

        StubbedStoryProvider(List<Story> stories) {
            this.stories = stories;
        }

        @Override
        public void readItems(@NotNull List<Integer> ids, @NotNull ValueCallback<? super Story> valueCallback) {
            for (Story story : stories) {
                valueCallback.onValueRetrieved(story);
            }
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
