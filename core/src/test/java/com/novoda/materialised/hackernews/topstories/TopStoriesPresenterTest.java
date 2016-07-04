package com.novoda.materialised.hackernews.topstories;

import com.novoda.materialised.hackernews.generics.AsyncListView;
import com.novoda.materialised.hackernews.generics.ClickListener;
import com.novoda.materialised.hackernews.generics.NoOpClickListener;
import com.novoda.materialised.hackernews.generics.ValueCallback;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.topstories.database.Story;
import com.novoda.materialised.hackernews.topstories.database.TopStoriesDatabase;
import com.novoda.materialised.hackernews.topstories.view.StoryViewData;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TopStoriesPresenterTest {

    private final long firstStoryId = 56L;
    private final long secondStoryId = 78L;
    private final List<Long> topStoryIds = Arrays.asList(firstStoryId, secondStoryId);

    private final Story aStory = buildStory();
    private final Story anotherStory = buildAnotherStory();
    private final int TEST_TIME = 3471394;

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewModels_WhenPresentingMultipleStories() {
        StoryViewModel firstIdOnlyViewModel = buildIdOnlyViewModel(firstStoryId);
        StoryViewModel secondIdOnlyViewModel = buildIdOnlyViewModel(secondStoryId);

        List<StoryViewModel> expectedViewModels = Arrays.asList(firstIdOnlyViewModel, secondIdOnlyViewModel);
        SpyingAsyncListView storiesView = new SpyingAsyncListView();

        presentWith(storiesView, Collections.<Story>emptyList());

        assertThat(storiesView.updatedStoryViewModels).isEqualTo(expectedViewModels);
    }

    @Test
    public void presenterGivesCorrectUpdatedViewModelsToView_OneAtATime_WhenPresentingMultipleStories() {
        SpyingAsyncListView storiesView = new SpyingAsyncListView();

        presentWith(storiesView, Arrays.asList(aStory, anotherStory));

        StoryViewModel storyViewModel = convert(aStory, new NoOpClickListener<StoryViewData>());
        StoryViewModel anotherStoryViewModel = convert(anotherStory, new NoOpClickListener<StoryViewData>());

        assertThat(storiesView.firstUpdatedStoryViewModel.getViewData()).isEqualTo(storyViewModel.getViewData());
        assertThat(storiesView.secondUpdatedStoryViewModel.getViewData()).isEqualTo(anotherStoryViewModel.getViewData());
    }

    private StoryViewModel convert(Story story, ClickListener<StoryViewData> clickListener) {
        StoryViewData storyViewData = new StoryViewData(story.getBy(), story.getKids(), story.getId(), story.getScore(), story.getTitle(), story.getUrl());
        return new StoryViewModel(storyViewData, clickListener);
    }

    private void presentWith(SpyingAsyncListView storiesView, List<Story> stories) {
        TopStoriesPresenter presenter = new TopStoriesPresenter(
                new StubbedTopStoriesDatabase(topStoryIds),
                new StubbedItemsDatabase(stories),
                storiesView,
                new SpyingNavigator()
        );
        presenter.present();
    }

    private Story buildStory() {
        return new Story("test author", 123, (int) firstStoryId, Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url");
    }

    private Story buildAnotherStory() {
        return new Story("another author", 456, (int) secondStoryId, Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url");
    }

    private StoryViewModel buildIdOnlyViewModel(long storyId) {
        StoryViewData empty = new StoryViewData();
        StoryViewData idOnly = new StoryViewData(empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl());
        return new StoryViewModel(idOnly, new NoOpClickListener<StoryViewData>());
    }

    private static class StubbedTopStoriesDatabase implements TopStoriesDatabase {
        public final List<Long> ids;

        private StubbedTopStoriesDatabase(List<Long> ids) {
            this.ids = ids;
        }

        @Override
        public void readAll(@NotNull ValueCallback<? super List<Long>> callback) {
            callback.onValueRetrieved(ids);
        }
    }

    private static class StubbedItemsDatabase implements ItemsDatabase {
        private List<Story> stories;

        public StubbedItemsDatabase(List<Story> stories) {
            this.stories = stories;
        }

        @Override
        public void readItem(int id, @NotNull ValueCallback<? super Story> valueCallback) {
            valueCallback.onValueRetrieved(stories.get(0));
        }

        @Override
        public void readItems(@NotNull List<Integer> ids, @NotNull ValueCallback<? super Story> valueCallback) {
            for (Story story : stories) {
                valueCallback.onValueRetrieved(story);
            }
        }
    }

    private static class SpyingAsyncListView implements AsyncListView<StoryViewModel> {
        List<com.novoda.materialised.hackernews.topstories.view.StoryViewModel> updatedStoryViewModels;
        com.novoda.materialised.hackernews.topstories.view.StoryViewModel firstUpdatedStoryViewModel;
        com.novoda.materialised.hackernews.topstories.view.StoryViewModel secondUpdatedStoryViewModel;

        @Override
        public void updateWith(List<com.novoda.materialised.hackernews.topstories.view.StoryViewModel> initialViewModelList) {
            updatedStoryViewModels = initialViewModelList;
        }

        @Override
        public void updateWith(com.novoda.materialised.hackernews.topstories.view.StoryViewModel viewModel) {
            if (firstUpdatedStoryViewModel == null) {
                firstUpdatedStoryViewModel = viewModel;
            } else if (secondUpdatedStoryViewModel == null) {
                secondUpdatedStoryViewModel = viewModel;
            }
        }
    }

    private static class SpyingNavigator implements Navigator {
        public String uri;

        @Override
        public void navigateTo(@NotNull String uri) {
            this.uri = uri;
        }
    }
}
