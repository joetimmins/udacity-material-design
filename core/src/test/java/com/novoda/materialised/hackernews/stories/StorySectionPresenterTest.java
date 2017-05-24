package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.stories.provider.IdOnlyStoryProvider;
import com.novoda.materialised.hackernews.stories.provider.Story;
import com.novoda.materialised.hackernews.stories.provider.StoryProvider;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.fest.assertions.api.Assertions.assertThat;

public class StorySectionPresenterTest {

    private static final int TEST_TIME = 3471394;
    private static final int FIRST_STORY_ID = 56;
    private static final int SECOND_STORY_ID = 78;
    private static final List<Story> ID_ONLY_STORIES = Arrays.asList(
            Story.IdOnly.buildFor(FIRST_STORY_ID),
            Story.IdOnly.buildFor(SECOND_STORY_ID)
    );

    private static final Story A_STORY = new Story("test author", 123, (int) FIRST_STORY_ID, Arrays.asList(1, 2), 123, TEST_TIME, "test title", "test type", "http://test.url");
    private static final Story ANOTHER_STORY = new Story("another author", 456, (int) SECOND_STORY_ID, Arrays.asList(3, 4), 456, TEST_TIME, "another title", "another type", "http://another.url");

    @Test
    public void presenterGivesCorrectListOfIdsToView_AsViewData_WhenPresentingMultipleStories() {
        StoryViewData firstIdOnlyViewData = buildIdOnlyViewData(FIRST_STORY_ID);
        StoryViewData secondIdOnlyViewData = buildIdOnlyViewData(SECOND_STORY_ID);
        List<StoryViewData> expectedViewData = Arrays.asList(firstIdOnlyViewData, secondIdOnlyViewData);

        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(ID_ONLY_STORIES, Collections.<Story>emptyList(), storiesView, new SpyingNavigator());

        assertThat(storiesView.receivedData).isEqualTo(expectedViewData);
    }

    @Test
    public void presenterTellsViewToShowErrorScreen_WhenNoIdOnlyStoriesAreRetrieved() {
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(Collections.<Story>emptyList(), Collections.<Story>emptyList(), storiesView, new SpyingNavigator());

        assertThat(storiesView.errorShown).isTrue();
    }

    @Test
    public void presenterGivesViewModelsWithFullViewDataToView_OneAtATime_WhenPresentingMultipleStories() {
        StoryViewData expectedViewData = createStoryViewDataFrom(A_STORY);
        StoryViewData anotherExpectedViewData = createStoryViewDataFrom(ANOTHER_STORY);
        SpyingStoriesView storiesView = new SpyingStoriesView();

        presentWith(ID_ONLY_STORIES, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, new SpyingNavigator());

        StoryViewData actualViewData = storiesView.firstUpdatedViewModel.getViewData();
        StoryViewData anotherActualViewData = storiesView.secondUpdatedViewModel.getViewData();

        assertThat(actualViewData).isEqualTo(expectedViewData);
        assertThat(anotherActualViewData).isEqualTo(anotherExpectedViewData);
    }

    @Test
    public void presenterGivesViewModelWithNavigatingClickListenerToView_WhenPresenting() {
        SpyingStoriesView storiesView = new SpyingStoriesView();
        SpyingNavigator navigator = new SpyingNavigator();

        presentWith(ID_ONLY_STORIES, Arrays.asList(A_STORY, ANOTHER_STORY), storiesView, navigator);

        storiesView.firstUpdatedViewModel.onClick();

        assertThat(navigator.uri).isEqualTo(A_STORY.getUrl());
    }

    private StoryViewData createStoryViewDataFrom(Story story) {
        return new StoryViewData(
                story.getBy(), story.getKids(), story.getId(), story.getScore(), story.getTitle(), story.getUrl()
        );
    }

    private void presentWith(List<Story> idOnlyStories, List<Story> stories, AsyncListView<StoryViewData> storiesView, Navigator navigator) {
        StorySectionPresenter presenter = new StorySectionPresenter(
                new StubbedIdOnlyStoryProvider(idOnlyStories),
                new StubbedStoryProvider(stories),
                storiesView,
                navigator,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );
        presenter.present(Section.NEW);
    }

    private StoryViewData buildIdOnlyViewData(int storyId) {
        StoryViewData empty = new StoryViewData();
        return new StoryViewData(
                empty.getBy(), empty.getCommentIds(), storyId, empty.getScore(), empty.getTitle(), empty.getUrl()
        );
    }

    private static class StubbedIdOnlyStoryProvider implements IdOnlyStoryProvider {
        final List<Story> idOnlyStories;

        private StubbedIdOnlyStoryProvider(List<Story> stories) {
            this.idOnlyStories = stories;
        }

        @NotNull
        @Override
        public Single<List<Story>> idOnlyStoriesFor(@NotNull Section section) {
            return Single.just(idOnlyStories);
        }
    }

    private static class StubbedStoryProvider implements StoryProvider {
        private List<Story> stories;

        StubbedStoryProvider(List<Story> stories) {
            this.stories = stories;
        }

        @NotNull
        @Override
        public Observable<Story> readItems(@NotNull List<Integer> ids) {
            return Observable.fromIterable(stories);
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
