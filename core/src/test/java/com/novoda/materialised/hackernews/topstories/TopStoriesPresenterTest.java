package com.novoda.materialised.hackernews.topstories;

import com.novoda.materialised.hackernews.ValueCallback;
import com.novoda.materialised.hackernews.items.ItemsDatabase;
import com.novoda.materialised.hackernews.items.StoryViewModel;

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

    private final StoryViewModel storyViewModel = buildStoryViewModel();
    private final StoryViewModel anotherStoryViewModel = buildAnotherStoryViewModel();

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewModels_WhenPresentingMultipleStories() {
        StoryViewModel firstIdOnlyViewModel = buildIdOnlyViewModel(firstStoryId);
        StoryViewModel secondIdOnlyViewModel = buildIdOnlyViewModel(secondStoryId);

        List<StoryViewModel> expectedViewModels = Arrays.asList(firstIdOnlyViewModel, secondIdOnlyViewModel);
        SpyingTopStoriesView storiesView = new SpyingTopStoriesView();

        TopStoriesPresenter presenter = new TopStoriesPresenter(
                new StubbedTopStoriesDatabase(topStoryIds),
                new StubbedItemsDatabase(Collections.<StoryViewModel>emptyList()),
                storiesView
        );
        presenter.present();

        assertThat(storiesView.updatedStoryViewModels).isEqualTo(expectedViewModels);
    }

    @Test
    public void presenterGivesCorrectUpdatedViewModelsToView_OneAtATime_WhenPresentingMultipleStories() {
        SpyingTopStoriesView storiesView = new SpyingTopStoriesView();

        TopStoriesPresenter presenter = new TopStoriesPresenter(
                new StubbedTopStoriesDatabase(topStoryIds),
                new StubbedItemsDatabase(Arrays.asList(storyViewModel, anotherStoryViewModel)),
                storiesView
        );
        presenter.present();

        assertThat(storiesView.firstUpdatedStoryViewModel).isEqualTo(storyViewModel);
        assertThat(storiesView.secondUpdatedStoryViewModel).isEqualTo(anotherStoryViewModel);
    }

    private StoryViewModel buildStoryViewModel() {
        return new StoryViewModel("test author", Arrays.asList(1, 2), (int) firstStoryId, 123, "test title", "http://test.url");
    }

    private StoryViewModel buildAnotherStoryViewModel() {
        return new StoryViewModel("another author", Arrays.asList(3, 4), (int) secondStoryId, 321, "another title", "http://another.url");
    }

    private StoryViewModel buildIdOnlyViewModel(long storyId) {
        StoryViewModel empty = new StoryViewModel();
        return new StoryViewModel(empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl());
    }

    private static class StubbedTopStoriesDatabase implements TopStoriesDatabase {
        public final List<Long> ids;

        private StubbedTopStoriesDatabase(List<Long> ids) {
            this.ids = ids;
        }

        @Override
        public void readAll(@NotNull ValueCallback<List<Long>> callback) {
            callback.onValueRetrieved(ids);
        }
    }

    private static class StubbedItemsDatabase implements ItemsDatabase {
        private List<StoryViewModel> storyViewModels;

        public StubbedItemsDatabase(List<StoryViewModel> expectedViewModels) {
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

    private static class SpyingTopStoriesView implements TopStoriesView {
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
