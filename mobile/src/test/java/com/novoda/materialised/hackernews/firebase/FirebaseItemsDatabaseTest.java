package com.novoda.materialised.hackernews.firebase;

import com.novoda.materialised.hackernews.topstories.database.ValueCallback;
import com.novoda.materialised.hackernews.topstories.database.Story;

import java.util.Arrays;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseItemsDatabaseTest {

    @Test
    public void readItemsCallsBackOncePerValueRetrieved() {
        int firstStoryId = 12;
        int secondStoryId = 34;
        CapturingValueCallback valueCallback = new CapturingValueCallback();
        Story firstStory = new Story("author", 890, firstStoryId, Arrays.asList(1, 2), 4, 1232, "test title", "test type", "http://test.url");
        Story secondStory = new Story("another author", 567, secondStoryId, Arrays.asList(3, 4), 5, 7897, "another title", "another type", "http://another.url");

        FirebaseItemsDatabase firebaseItemsDatabase = new FirebaseItemsDatabase(FakeFirebase.getItemsDatabase(Arrays.asList(firstStory, secondStory)));
        firebaseItemsDatabase.readItems(Arrays.asList(firstStoryId, secondStoryId), valueCallback);

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
