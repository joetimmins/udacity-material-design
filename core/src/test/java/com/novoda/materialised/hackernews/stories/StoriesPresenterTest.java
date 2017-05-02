package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.stories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.stories.database.Story;
import com.novoda.materialised.hackernews.stories.database.StoryIdDatabase;
import com.novoda.materialised.hackernews.stories.database.ValueCallback;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static org.fest.assertions.api.Assertions.assertThat;

public class StoriesPresenterTest {

    private static final int TEST_TIME = 3471394;
    private static final long FIRST_STORY_ID = 56L;
    private static final long SECOND_STORY_ID = 78L;
    private static final List<Long> TOP_STORY_IDS = Arrays.asList(FIRST_STORY_ID, SECOND_STORY_ID);

    private static final Story A_STORY = new Story("test author", 123, (int) FIRST_STORY_ID, Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url");
    private static final Story ANOTHER_STORY = new Story("another author", 456, (int) SECOND_STORY_ID, Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url");

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewModels_WhenPresentingMultipleStories() {
        ViewModel<StoryViewData> firstIdOnlyViewModel = buildIdOnlyViewModel(FIRST_STORY_ID);
        ViewModel<StoryViewData> secondIdOnlyViewModel = buildIdOnlyViewModel(SECOND_STORY_ID);

        List<ViewModel<StoryViewData>> expectedViewModels = Arrays.asList(firstIdOnlyViewModel, secondIdOnlyViewModel);
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(TOP_STORY_IDS, Collections.<Story>emptyList(), storiesView, new SpyingNavigator());

        assertThat(storiesView.updatedStoryViewModels).isEqualTo(expectedViewModels);
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
        StoriesPresenter presenter = new StoriesPresenter(
                new StubbedStoryIdDatabase(topStoryIds),
                new StubbedItemsDatabase(stories),
                storiesView,
                navigator
        );
        presenter.present(Section.NEW);
    }

    private ViewModel<StoryViewData> buildIdOnlyViewModel(long storyId) {
        StoryViewData empty = new StoryViewData();
        StoryViewData idOnly = new StoryViewData(
                empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl()
        );
        return new ViewModel<>(idOnly, new Function1<StoryViewData, Unit>() {
            @Override
            public Unit invoke(StoryViewData storyViewData) {
                return null;
            }
        });
    }

    private static class StubbedStoryIdDatabase implements StoryIdDatabase {
        final List<Long> ids;

        private StubbedStoryIdDatabase(List<Long> ids) {
            this.ids = ids;
        }

        @Override
        public void readStoryIds(@NotNull Section section, @NotNull ValueCallback<? super List<Long>> callback) {
            callback.onValueRetrieved(ids);
        }
    }

    private static class StubbedItemsDatabase implements ItemsDatabase {
        private List<Story> stories;

        StubbedItemsDatabase(List<Story> stories) {
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
        List<ViewModel<StoryViewData>> updatedStoryViewModels;
        ViewModel<StoryViewData> firstUpdatedViewModel;
        ViewModel<StoryViewData> secondUpdatedViewModel;
        boolean errorShown;

        @Override
        public void updateWith(@NotNull List<ViewModel<StoryViewData>> initialViewModelList) {
            updatedStoryViewModels = initialViewModelList;
        }

        @Override
        public void updateWith(ViewModel<StoryViewData> viewModel) {
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
