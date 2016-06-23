package com.novoda.materialised.hackernews.topstories;

import com.novoda.materialised.hackernews.ValueCallback;
import com.novoda.materialised.hackernews.items.ItemsDatabase;
import com.novoda.materialised.hackernews.items.StoryViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TopStoriesPresenterTest {

    private static final long firstStoryId = 56L;
    private static final long secondStoryId = 78L;

    private static final StoryViewModel storyViewModel = new StoryViewModel("test author", Arrays.asList(1, 2), (int) firstStoryId, 123, "test title", "http://test.url");
    private static final StoryViewModel anotherStoryViewModel = new StoryViewModel("another author", Arrays.asList(3, 4), (int) secondStoryId, 321, "another title", "http://another.url");

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewModels_WhenPresentingMultipleStories() {
        StoryViewModel firstIdOnlyViewModel = buildIdOnlyViewModel(firstStoryId);
        StoryViewModel secondIdOnlyViewModel = buildIdOnlyViewModel(secondStoryId);

        List<StoryViewModel> expectedViewModels = Arrays.asList(firstIdOnlyViewModel, secondIdOnlyViewModel);
        CapturingStoriesView storiesView = new CapturingStoriesView();

        TopStoriesPresenter presenter = new TopStoriesPresenter(new DummyTopStoriesDatabase(), new ConfigurableItemsDatabase(new ArrayList<StoryViewModel>()));
        presenter.presentMultipleStoriesWith(storiesView);

        assertThat(storiesView.updatedStoryViewModels).isEqualTo(expectedViewModels);
    }

    @Test
    public void presenterGivesCorrectUpdatedViewModelsToView_OneAtATime_WhenPresentingMultipleStories() {
        CapturingStoriesView storiesView = new CapturingStoriesView();

        TopStoriesPresenter presenter = new TopStoriesPresenter(new DummyTopStoriesDatabase(), new ConfigurableItemsDatabase(Arrays.asList(storyViewModel, anotherStoryViewModel)));
        presenter.presentMultipleStoriesWith(storiesView);

        assertThat(storiesView.firstUpdatedStoryViewModel).isEqualTo(storyViewModel);
        assertThat(storiesView.secondUpdatedStoryViewModel).isEqualTo(anotherStoryViewModel);
    }

    private StoryViewModel buildIdOnlyViewModel(long storyId) {
        StoryViewModel empty = new StoryViewModel();
        return new StoryViewModel(empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl());
    }

    private static class DummyTopStoriesDatabase implements TopStoriesDatabase {
        public final List<Long> topStories = Arrays.asList(firstStoryId, secondStoryId);

        @Override
        public void readAll(@NotNull ValueCallback<List<Long>> callback) {
            callback.onValueRetrieved(topStories);
        }
    }

    private static class ConfigurableItemsDatabase implements ItemsDatabase {
        private List<StoryViewModel> storyViewModels;

        public ConfigurableItemsDatabase(List<StoryViewModel> expectedViewModels) {
            this.storyViewModels = expectedViewModels;
        }

        @Override
        public void readItem(int id, @NotNull ValueCallback<StoryViewModel> valueCallback) {
            valueCallback.onValueRetrieved(storyViewModels.get(0));
        }

        @Override
        public void readItems(@NotNull List<Integer> ids, @NotNull ValueCallback<StoryViewModel> valueCallback) {
            for (StoryViewModel viewModel : storyViewModels) {
                valueCallback.onValueRetrieved(viewModel);
            }
        }
    }

    private static class CapturingStoriesView implements StoriesView {
        List<StoryViewModel> updatedStoryViewModels;
        StoryViewModel firstUpdatedStoryViewModel;
        StoryViewModel secondUpdatedStoryViewModel;

        @Override
        public void updateWith(@NotNull List<StoryViewModel> storyViewModels) {
            updatedStoryViewModels = storyViewModels;
        }

        @Override
        public void updateWith(@NotNull StoryViewModel storyViewModel) {
            if (firstUpdatedStoryViewModel == null) {
                firstUpdatedStoryViewModel = storyViewModel;
            } else if (secondUpdatedStoryViewModel == null) {
                secondUpdatedStoryViewModel = storyViewModel;
            }
        }
    }
}
