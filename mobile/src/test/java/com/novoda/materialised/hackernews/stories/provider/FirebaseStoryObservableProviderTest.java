package com.novoda.materialised.hackernews.stories.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseStoryObservableProviderTest {

    @Test
    public void readItemsCallsBackOncePerValueRetrieved() {
        int firstStoryId = 12;
        int secondStoryId = 34;
        Story firstStory = new Story("author", 890, firstStoryId, Arrays.asList(1, 2), 4, 1232, "test title", "test type", "http://test.url");
        Story secondStory = new Story("another author", 567, secondStoryId, Arrays.asList(3, 4), 5, 7897, "another title", "another type", "http://another.url");
        List<Story> stories = Arrays.asList(firstStory, secondStory);
        FirebaseStoryObservableProvider provider = new FirebaseStoryObservableProvider(FakeFirebase.getItemsDatabase(stories));

        List<Observable<Story>> storyObservables = provider
                .createStoryObservables(Arrays.asList(firstStoryId, secondStoryId));

        final List<Story> actualStories = new ArrayList<>();

        for (Observable<Story> storyObservable : storyObservables) {
            storyObservable.subscribe(new OnNextObserver<Story>() {
                @Override
                public void onNext(@NonNull Story story) {
                    actualStories.add(story);
                }
            });
        }

        assertThat(actualStories).isEqualTo(stories);
    }

    private static abstract class OnNextObserver<T> implements Observer<T> {
        @Override
        public final void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public final void onError(@NonNull Throwable e) {

        }

        @Override
        public final void onComplete() {

        }
    }
}
