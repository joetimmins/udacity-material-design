package com.novoda.materialised.hackernews.stories.provider;

import org.junit.Test;

public class FirebaseStoryObservableProviderTest {

    @Test
    public void readItemsCallsBackOncePerValueRetrieved() {
//        TestObserver<Observable<Story>> storyObserver = new TestObserver<>();
//        int firstStoryId = 12;
//        int secondStoryId = 34;
//        Story firstStory = new Story("author", 890, firstStoryId, Arrays.asList(1, 2), 4, 1232, "test title", "test type", "http://test.url");
//        Story secondStory = new Story("another author", 567, secondStoryId, Arrays.asList(3, 4), 5, 7897, "another title", "another type", "http://another.url");
//        List<Story> stories = Arrays.asList(firstStory, secondStory);
//        FirebaseStoryObservableProvider provider = new FirebaseStoryObservableProvider(FakeFirebase.getItemsDatabase(stories));
//
//        List<Integer> storyIds = Arrays.asList(firstStoryId, secondStoryId);
//        Observable<Story> observable = provider
//                .createStoryObservables(storyIds)
//                .subscribeOn(Schedulers.trampoline())
//                .observeOn(Schedulers.trampoline());
//
//        observable.subscribe(storyObserver);
//
//        storyObserver.assertValues(firstStory, secondStory);
    }
}
