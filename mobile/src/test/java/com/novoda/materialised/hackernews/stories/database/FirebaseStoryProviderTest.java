package com.novoda.materialised.hackernews.stories.database;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseStoryProviderTest {

    @Test
    public void readItemsCallsBackOncePerValueRetrieved() {
        int firstStoryId = 12;
        int secondStoryId = 34;
        Story firstStory = new Story("author", 890, firstStoryId, Arrays.asList(1, 2), 4, 1232, "test title", "test type", "http://test.url");
        Story secondStory = new Story("another author", 567, secondStoryId, Arrays.asList(3, 4), 5, 7897, "another title", "another type", "http://another.url");
        List<Story> stories = Arrays.asList(firstStory, secondStory);
        FirebaseStoryProvider firebaseItemsDatabase = new FirebaseStoryProvider(FakeFirebase.getItemsDatabase(stories));

        List<Integer> storyIds = Arrays.asList(firstStoryId, secondStoryId);
        CapturingValueCallback valueCallback = new CapturingValueCallback();
        firebaseItemsDatabase.readItems(storyIds, valueCallback);

        assertThat(valueCallback.first).isEqualTo(firstStory);
        assertThat(valueCallback.second).isEqualTo(secondStory);
    }

    private class CapturingValueCallback implements ValueCallback<Story> {
        Story first;
        Story second;

        @Override
        public void onValueRetrieved(Story value) {
            if (first == null) {
                first = value;
            } else if (second == null) {
                second = value;
            }
        }
    }
}
