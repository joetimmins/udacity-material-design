package com.novoda.materialised.hackernews.topstories;

import com.novoda.materialised.hackernews.ClickListener;
import com.novoda.materialised.hackernews.NoOpClickListener;
import com.novoda.materialised.hackernews.ValueCallback;
import com.novoda.materialised.hackernews.items.ItemsDatabase;
import com.novoda.materialised.hackernews.items.Story;
import com.novoda.materialised.hackernews.items.StoryViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.novoda.materialised.hackernews.items.StoryToViewModelConverterKt.convertStoryToViewModel;
import static org.fest.assertions.api.Assertions.assertThat;

public class TopStoriesPresenterTest {

    private final long firstStoryId = 56L;
    private final long secondStoryId = 78L;
    private final List<Long> topStoryIds = Arrays.asList(firstStoryId, secondStoryId);

    private final Story story = buildStory();
    private final Story anotherStory = buildAnotherStory();
    private final int TEST_TIME = 3471394;

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewModels_WhenPresentingMultipleStories() {
        StoryViewModel firstIdOnlyViewModel = buildIdOnlyViewModel(firstStoryId);
        StoryViewModel secondIdOnlyViewModel = buildIdOnlyViewModel(secondStoryId);

        List<StoryViewModel> expectedViewModels = Arrays.asList(firstIdOnlyViewModel, secondIdOnlyViewModel);
        SpyingTopStoriesView storiesView = new SpyingTopStoriesView();

        presentWith(storiesView, Collections.<Story>emptyList());

        assertThat(storiesView.updatedStoryViewModels).isEqualTo(expectedViewModels);
    }

    @Test
    public void presenterGivesCorrectUpdatedViewModelsToView_OneAtATime_WhenPresentingMultipleStories() {
        SpyingTopStoriesView storiesView = new SpyingTopStoriesView();

        presentWith(storiesView, Arrays.asList(story, anotherStory));

        StoryViewModel storyViewModel = convertStoryToViewModel(story);
        StoryViewModel anotherStoryViewModel = convertStoryToViewModel(anotherStory);

        assertThat(storiesView.firstUpdatedStoryViewModel).isEqualTo(storyViewModel);
        assertThat(storiesView.secondUpdatedStoryViewModel).isEqualTo(anotherStoryViewModel);
    }

    private void presentWith(SpyingTopStoriesView storiesView, List<Story> stories) {
        TopStoriesPresenter presenter = new TopStoriesPresenter(
                new StubbedTopStoriesDatabase(topStoryIds),
                new StubbedItemsDatabase(stories),
                storiesView,
                new NoOpClickListener<StoryViewModel>()
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
        StoryViewModel empty = new StoryViewModel();
        return new StoryViewModel(empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl(), empty.getClickListener());
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
        private List<Story> stories;

        public StubbedItemsDatabase(List<Story> stories) {
            this.stories = stories;
        }

        @Override
        public void readItem(int id, @NotNull ValueCallback<Story> valueCallback) {
            valueCallback.onValueRetrieved(stories.get(0));
        }

        @Override
        public void readItems(@NotNull List<Integer> ids, @NotNull ValueCallback<Story> valueCallback) {
            for (Story story : stories) {
                valueCallback.onValueRetrieved(story);
            }
        }
    }

    private static class SpyingTopStoriesView implements TopStoriesView {
        List<StoryViewModel> updatedStoryViewModels;
        StoryViewModel firstUpdatedStoryViewModel;
        StoryViewModel secondUpdatedStoryViewModel;

        @Override
        public void updateWith(@NotNull List<StoryViewModel> initialViewModelList) {
            updatedStoryViewModels = initialViewModelList;
        }

        @Override
        public void updateWith(@NotNull StoryViewModel storyViewModel, @NotNull ClickListener<StoryViewModel> clickListener) {
            if (firstUpdatedStoryViewModel == null) {
                firstUpdatedStoryViewModel = storyViewModel;
            } else if (secondUpdatedStoryViewModel == null) {
                secondUpdatedStoryViewModel = storyViewModel;
            }
        }
    }
}
