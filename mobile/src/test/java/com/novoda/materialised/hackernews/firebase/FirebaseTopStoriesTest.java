package com.novoda.materialised.hackernews.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.topstories.database.ValueCallback;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseTopStoriesTest {

    @Mock
    FirebaseDatabase mockFirebaseDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testThatTopStoriesCallsBackWithIdList() {
        // Arrange
        List<Long> expectedStoryIds = Arrays.asList(8863L, 9001L, 9004L);
        ListValueCallback callback = new ListValueCallback();

        // Act
        new FirebaseTopStoriesDatabase(FakeFirebase.getTopStoriesFirebaseDatabase(expectedStoryIds)).readTopStoriesIds(callback);

        // Assert
        assertThat(callback.topStoryIds).isEqualTo(expectedStoryIds);
    }

    private static class ListValueCallback implements ValueCallback<List<Long>> {

        public List<Long> topStoryIds;

        @Override
        public void onValueRetrieved(List<Long> value) {
            topStoryIds = value;
        }
    }
}
