package com.novoda.materialised.hackernews.stories.provider;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

public class FirebaseStoryProviderTest {

    @Test
    public void readItemsCallsBackOncePerValueRetrieved() {
        TestObserver<Story> storyObserver = new TestObserver<>();
        int firstStoryId = 12;
        int secondStoryId = 34;
        Story firstStory = new Story("author", 890, firstStoryId, Arrays.asList(1, 2), 4, 1232, "test title", "test type", "http://test.url");
        Story secondStory = new Story("another author", 567, secondStoryId, Arrays.asList(3, 4), 5, 7897, "another title", "another type", "http://another.url");
        List<Story> stories = Arrays.asList(firstStory, secondStory);
        FirebaseStoryProvider firebaseItemsDatabase = new FirebaseStoryProvider(FakeFirebase.getItemsDatabase(stories));

        List<Integer> storyIds = Arrays.asList(firstStoryId, secondStoryId);
        Observable<Story> observable = firebaseItemsDatabase
                .readItems(storyIds)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline());

        observable.subscribe(storyObserver);

        storyObserver.assertValues(firstStory, secondStory);
    }
}
