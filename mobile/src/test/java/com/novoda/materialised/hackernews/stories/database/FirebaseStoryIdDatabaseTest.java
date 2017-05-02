package com.novoda.materialised.hackernews.stories.database;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseStoryIdDatabaseTest {

    @Test
    public void testThatTopStoriesCallsBackWithIdList() {
        // Arrange
        List<Long> expectedStoryIds = Arrays.asList(8863L, 9001L, 9004L);
        ListValueCallback callback = new ListValueCallback();
        FirebaseDatabase storyTypeFirebaseDatabase = FakeFirebase.getDatabaseForStoryType(Section.BEST, expectedStoryIds);

        // Act
        new FirebaseStoryIdDatabase(storyTypeFirebaseDatabase).readStoryIds(Section.BEST, callback);

        // Assert
        assertThat(callback.topStoryIds).isEqualTo(expectedStoryIds);
    }

    private static class ListValueCallback implements ValueCallback<List<Long>> {

        List<Long> topStoryIds;

        @Override
        public void onValueRetrieved(List<Long> value) {
            topStoryIds = value;
        }
    }
}
