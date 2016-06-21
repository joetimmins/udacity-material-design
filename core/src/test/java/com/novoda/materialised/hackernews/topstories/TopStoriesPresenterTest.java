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

    @Test
    public void testPresenter() {
        final StoryViewModel expectedStoryViewModel = new StoryViewModel("test author", Arrays.asList(1, 2), 123, "test title", "http://test.url");
        CapturingStoryView storyView = new CapturingStoryView();

        new TopStoriesPresenter(new DummyTopStoriesDatabase(), new ConfigurableItemsDatabase(expectedStoryViewModel), storyView).startPresenting();

        assertThat(storyView.updatedWith).isEqualTo(expectedStoryViewModel);
    }

    private static class CapturingStoryView implements StoryView {

        public StoryViewModel updatedWith;

        @Override
        public void updateWith(@NotNull StoryViewModel storyViewModel) {
            updatedWith = storyViewModel;
        }
    }

    private static class DummyTopStoriesDatabase implements TopStoriesDatabase {
        @Override
        public void readAll(@NotNull ValueCallback<List<Long>> callback) {
            callback.onValueRetrieved(Arrays.asList(56L, 78L));
        }
    }

    private static class ConfigurableItemsDatabase implements ItemsDatabase {
        private final StoryViewModel expectedStoryViewModel;

        public ConfigurableItemsDatabase(StoryViewModel expectedStoryViewModel) {
            this.expectedStoryViewModel = expectedStoryViewModel;
        }

        @Override
        public void readItem(int id, @NotNull ValueCallback<StoryViewModel> valueCallback) {
            valueCallback.onValueRetrieved(expectedStoryViewModel);
        }
    }
}
