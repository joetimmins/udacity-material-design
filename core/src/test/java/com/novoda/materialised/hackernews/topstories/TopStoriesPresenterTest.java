package com.novoda.materialised.hackernews.topstories;

import com.novoda.materialised.hackernews.ValueCallback;
import com.novoda.materialised.hackernews.items.ItemsDatabase;
import com.novoda.materialised.hackernews.items.StoryViewModel;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TopStoriesPresenterTest {

    private final StoryViewModel storyViewModel = new StoryViewModel("test author", Arrays.asList(1, 2), 123, "test title", "http://test.url");

    @Test
    public void presenterGivesCorrectViewModelToView_WhenPresentingSingleStory() {
        CapturingStoryView storyView = new CapturingStoryView();

        TopStoriesPresenter presenter = new TopStoriesPresenter(new DummyTopStoriesDatabase(), new ConfigurableItemsDatabase(storyViewModel));
        presenter.presentSingleStoryWith(storyView);

        assertThat(storyView.updatedWith).isEqualTo(storyViewModel);
    }

    @Test
    public void presenterGivesCorrectListOfViewModelsToView_WhenPresentingMultipleStories() {
        StoryViewModel anotherStoryViewModel = new StoryViewModel("another author", Arrays.asList(3, 4), 321, "another title", "http://another.url");
        List<StoryViewModel> expectedViewModels = Arrays.asList(storyViewModel, anotherStoryViewModel);
        CapturingStoriesView storiesView = new CapturingStoriesView();

        TopStoriesPresenter presenter = new TopStoriesPresenter(new DummyTopStoriesDatabase(), new ConfigurableItemsDatabase(expectedViewModels));
        presenter.presentMultipleStoriesWith(storiesView);

        assertThat(storiesView.updatedStoryCount).isEqualTo(DummyTopStoriesDatabase.TOP_STORIES.size());
        assertThat(storiesView.updatedStoryModels).isEqualTo(expectedViewModels);
    }

    private static class CapturingStoryView implements StoryView {

        public StoryViewModel updatedWith;

        @Override
        public void updateWith(@NotNull StoryViewModel storyViewModel) {
            updatedWith = storyViewModel;
        }
    }

    private static class DummyTopStoriesDatabase implements TopStoriesDatabase {
        public static final List<Long> TOP_STORIES = Arrays.asList(56L, 78L);

        @Override
        public void readAll(@NotNull ValueCallback<List<Long>> callback) {
            callback.onValueRetrieved(TOP_STORIES);
        }
    }

    private static class ConfigurableItemsDatabase implements ItemsDatabase {
        private StoryViewModel storyViewModel;

        private List<StoryViewModel> storyViewModels;

        public ConfigurableItemsDatabase(StoryViewModel expectedStoryViewModel) {
            this.storyViewModel = expectedStoryViewModel;
        }

        public ConfigurableItemsDatabase(List<StoryViewModel> expectedViewModels) {
            this.storyViewModels = expectedViewModels;
        }

        @Override
        public void readItem(int id, @NotNull ValueCallback<StoryViewModel> valueCallback) {
            valueCallback.onValueRetrieved(storyViewModel);
        }

        @Override
        public void readItems(@NotNull List<Integer> ids, @NotNull ValueCallback<List<StoryViewModel>> valueCallback) {
            valueCallback.onValueRetrieved(storyViewModels);
        }
    }

    private static class CapturingStoriesView implements StoriesView {
        List<StoryViewModel> updatedStoryModels;
        int updatedStoryCount;

        @Override
        public void updateWith(@NotNull List<StoryViewModel> storyViewModels) {
            updatedStoryModels = storyViewModels;
        }

        @Override
        public void updateWith(int numberOfStories) {
            updatedStoryCount = numberOfStories;
        }
    }
}
