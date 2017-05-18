package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

public class FirebaseIdOnlyStoryProviderTest {

    @Test
    public void testThatTopStoriesCallsBackWithIdList() {
        // Arrange
        TestObserver<List<Story>> testObserver = new TestObserver<>();
        List<Long> expectedStoryIds = Arrays.asList(8863L, 9001L, 9004L);
        List<Story> expectedIdOnlyStories = Arrays.asList(
                Story.IdOnly.buildFor(8863),
                Story.IdOnly.buildFor(9001),
                Story.IdOnly.buildFor(9004)
        );

        IdOnlyStoryValueCallback callback = new IdOnlyStoryValueCallback();
        FirebaseDatabase storyTypeFirebaseDatabase = FakeFirebase.getDatabaseForStoryType(Section.BEST, expectedStoryIds);

        // Act
        Observable<List<Story>> observable = new FirebaseIdOnlyStoryProvider(storyTypeFirebaseDatabase).readStoryIds(Section.BEST, callback);
        observable.subscribe(testObserver);

        // Assert
        testObserver.assertValue(expectedIdOnlyStories);
    }

    private static class IdOnlyStoryValueCallback implements ValueCallback<List<Story>> {

        List<Story> stories;

        @Override
        public void onValueRetrieved(List<Story> value) {
            stories = value;
        }
    }
}
