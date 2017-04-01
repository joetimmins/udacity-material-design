package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.ClickListener;
import com.novoda.materialised.hackernews.asynclistview.NoOpClickListener;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.stories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.stories.database.Story;
import com.novoda.materialised.hackernews.stories.database.StoryIdDatabase;
import com.novoda.materialised.hackernews.stories.database.ValueCallback;
import com.novoda.materialised.hackernews.stories.view.StoryClickListener;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;
import com.novoda.materialised.hackernews.stories.view.StoryViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StoriesPresenterTest {

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

        presentWith(topStoryIds, Collections.<Story>emptyList(), storiesView);

        assertThat(storiesView.updatedStoryViewModels).isEqualTo(expectedViewModels);
    }

    @Test
    public void presenterTellsViewToShowErrorScreen_WhenNoStoryIdsAreRetrieved() {
        SpyingAsyncListView storiesView = new SpyingAsyncListView();

        presentWith(Collections.<Long>emptyList(), Collections.<Story>emptyList(), storiesView);

        assertThat(storiesView.errorShown).isTrue();
    }

    @Test
    public void presenterGivesCorrectUpdatedViewModelsToView_OneAtATime_WhenPresentingMultipleStories() {
        SpyingAsyncListView storiesView = new SpyingAsyncListView();

        presentWith(topStoryIds, Arrays.asList(aStory, anotherStory), storiesView);

        StoryViewModel storyViewModel = convert(aStory, new StoryClickListener(spyingNavigator));
        StoryViewModel anotherStoryViewModel = convert(anotherStory, new StoryClickListener(spyingNavigator));

        assertThat(storiesView.firstUpdatedStoryViewModel).isEqualTo(storyViewModel);
        assertThat(storiesView.secondUpdatedStoryViewModel).isEqualTo(anotherStoryViewModel);
    }

    private StoryViewModel convert(Story story, ClickListener<StoryViewData> clickListener) {
        StoryViewData storyViewData = new StoryViewData(
                story.getBy(), story.getKids(), story.getId(), story.getScore(), story.getTitle(), story.getUrl()
        );
        return new StoryViewModel(storyViewData, clickListener);
    }

    private void presentWith(List<Long> topStoryIds, List<Story> stories, SpyingAsyncListView storiesView) {
        StoriesPresenter presenter = new StoriesPresenter(
                new StubbedStoryIdDatabase(topStoryIds),
                new StubbedItemsDatabase(stories),
                storiesView,
                spyingNavigator
        );
        presenter.present("anything");
    }

    private Story buildStory() {
        return new Story("test author", 123, (int) firstStoryId, Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url");
    }

    private Story buildAnotherStory() {
        return new Story("another author", 456, (int) secondStoryId, Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url");
    }

    private StoryViewModel buildIdOnlyViewModel(long storyId) {
        StoryViewData empty = new StoryViewData();
        StoryViewData idOnly = new StoryViewData(
                empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl()
        );
        return new StoryViewModel(idOnly, NoOpClickListener.INSTANCE);
    }

    private static class StubbedStoryIdDatabase implements StoryIdDatabase {
        final List<Long> ids;

        private StubbedStoryIdDatabase(List<Long> ids) {
            this.ids = ids;
        }

        @Override
        public void readStoryIds(@NotNull String storyType, @NotNull ValueCallback<? super List<Long>> callback) {
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

    private static class SpyingAsyncListView implements AsyncListView<StoryViewModel> {
        List<StoryViewModel> updatedStoryViewModels;
        StoryViewModel firstUpdatedStoryViewModel;
        StoryViewModel secondUpdatedStoryViewModel;
        boolean errorShown;

        @Override
        public void updateWith(List<StoryViewModel> initialViewModelList) {
            updatedStoryViewModels = initialViewModelList;
        }

        @Override
        public void updateWith(StoryViewModel viewModel) {
            if (firstUpdatedStoryViewModel == null) {
                firstUpdatedStoryViewModel = viewModel;
            } else if (secondUpdatedStoryViewModel == null) {
                secondUpdatedStoryViewModel = viewModel;
            }
        }

        @Override
        public void showError() {
            errorShown = true;
        }
    }

    private static final Navigator spyingNavigator = new Navigator() {
        String uri;

        @Override
        public void navigateTo(@NotNull String uri) {
            this.uri = uri;
        }
    };
}
