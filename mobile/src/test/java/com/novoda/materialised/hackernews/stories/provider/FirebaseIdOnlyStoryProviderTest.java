package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseIdOnlyStoryProviderTest {

    @Test
    public void testThatTopStoriesCallsBackWithIdList() {
        // Arrange
        List<Long> expectedStoryIds = Arrays.asList(8863L, 9001L, 9004L);
        ListValueCallback callback = new ListValueCallback();
        List<Story> expectedIdOnlyStories = Arrays.asList(
                Story.IdOnly.buildFor(8863),
                Story.IdOnly.buildFor(9001),
                Story.IdOnly.buildFor(9004)

        );
        FirebaseDatabase storyTypeFirebaseDatabase = FakeFirebase.getDatabaseForStoryType(Section.BEST, expectedStoryIds);

        // Act
        new FirebaseIdOnlyStoryProvider(storyTypeFirebaseDatabase).idOnlyStoriesFor(Section.BEST, callback);

        // Assert
        assertThat(callback.stories).isEqualTo(expectedIdOnlyStories);
    }

    private static class ListValueCallback implements ValueCallback<List<Story>> {

        List<Story> stories;

        @Override
        public void onValueRetrieved(List<Story> value) {
            stories = value;
        }
    }
}
