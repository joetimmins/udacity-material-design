package com.novoda.materialised.hackernews.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.topstories.database.ValueCallback;

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
        String topstories = "anything";
        FirebaseDatabase topStoriesFirebaseDatabase = FakeFirebase.getDatabaseForStoryType(topstories, expectedStoryIds);

        // Act
        new FirebaseStoryIdDatabase(topStoriesFirebaseDatabase).readStoryIds(topstories, callback);

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
