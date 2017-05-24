package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

public class FirebaseIdOnlyStoryProviderTest {

    @Test
    public void testThatReadingStoryIdsForSection_callsBackWithIdOnlyStoryList() {
        // Arrange
        TestObserver<List<Story>> testObserver = new TestObserver<>();
        List<Long> expectedStoryIds = Arrays.asList(8863L, 9001L, 9004L);
        List<Story> expectedIdOnlyStories = Arrays.asList(
                Story.IdOnly.buildFor(8863),
                Story.IdOnly.buildFor(9001),
                Story.IdOnly.buildFor(9004)
        );

        FirebaseDatabase storyTypeFirebaseDatabase = FakeFirebase.getDatabaseForStoryType(Section.BEST, expectedStoryIds);

        // Act
        FirebaseIdOnlyStoryProvider provider = new FirebaseIdOnlyStoryProvider(storyTypeFirebaseDatabase);
        Single<List<Story>> idOnlyStories = provider.idOnlyStoriesFor(Section.BEST);
        idOnlyStories.subscribe(testObserver);

        // Assert
        testObserver.assertValue(expectedIdOnlyStories);
    }
}
