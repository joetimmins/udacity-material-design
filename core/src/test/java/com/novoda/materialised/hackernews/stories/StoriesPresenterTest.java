package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.ClickListener;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.asynclistview.NoOpClickListener;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.stories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.stories.database.Story;
import com.novoda.materialised.hackernews.stories.database.StoryIdDatabase;
import com.novoda.materialised.hackernews.stories.database.ValueCallback;
import com.novoda.materialised.hackernews.stories.view.StoryClickListener;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

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
        ViewModel<StoryViewData> firstIdOnlyViewModel = buildIdOnlyViewModel(firstStoryId);
        ViewModel<StoryViewData> secondIdOnlyViewModel = buildIdOnlyViewModel(secondStoryId);

        List<ViewModel<StoryViewData>> expectedViewModels = Arrays.asList(firstIdOnlyViewModel, secondIdOnlyViewModel);
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(topStoryIds, Collections.<Story>emptyList(), storiesView);

        assertThat(storiesView.updatedStoryViewModels).isEqualTo(expectedViewModels);
    }

    @Test
    public void presenterTellsViewToShowErrorScreen_WhenNoStoryIdsAreRetrieved() {
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(Collections.<Long>emptyList(), Collections.<Story>emptyList(), storiesView);

        assertThat(storiesView.errorShown).isTrue();
    }

    @Test
    public void presenterGivesCorrectUpdatedViewModelsToView_OneAtATime_WhenPresentingMultipleStories() {
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(topStoryIds, Arrays.asList(aStory, anotherStory), storiesView);

        ViewModel<StoryViewData> storyViewModel = convert(aStory, new StoryClickListener(spyingNavigator));
        ViewModel<StoryViewData> anotherStoryViewModel = convert(anotherStory, new StoryClickListener(spyingNavigator));

        assertThat(storiesView.firstUpdatedStoryViewModel).isEqualTo(storyViewModel);
        assertThat(storiesView.secondUpdatedStoryViewModel).isEqualTo(anotherStoryViewModel);
    }

    private ViewModel<StoryViewData> convert(Story story, ClickListener<StoryViewData> clickListener) {
        StoryViewData storyViewData = new StoryViewData(
                story.getBy(), story.getKids(), story.getId(), story.getScore(), story.getTitle(), story.getUrl()
        );
        return new ViewModel<>(storyViewData, clickListener);
    }

    private void presentWith(List<Long> topStoryIds, List<Story> stories, SpyingStoriesView storiesView) {
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

    private ViewModel<StoryViewData> buildIdOnlyViewModel(long storyId) {
        StoryViewData empty = new StoryViewData();
        StoryViewData idOnly = new StoryViewData(
                empty.getBy(), empty.getCommentIds(), (int) storyId, empty.getScore(), empty.getTitle(), empty.getUrl()
        );
        return new ViewModel<>(idOnly, NoOpClickListener.INSTANCE);
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

    private static class SpyingStoriesView implements AsyncListView<StoryViewData> {
        List<ViewModel<StoryViewData>> updatedStoryViewModels;
        ViewModel<StoryViewData> firstUpdatedStoryViewModel;
        ViewModel<StoryViewData> secondUpdatedStoryViewModel;
        boolean errorShown;

        @Override
        public void updateWith(List<ViewModel<StoryViewData>> initialViewModelList) {
            updatedStoryViewModels = initialViewModelList;
        }

        @Override
        public void updateWith(ViewModel<StoryViewData> viewModel) {
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
