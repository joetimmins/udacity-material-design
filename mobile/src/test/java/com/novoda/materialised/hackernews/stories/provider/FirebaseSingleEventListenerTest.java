package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseSingleEventListenerTest {

    @Test
    public void invokeValueCallbackWithCorrectValue_whenDataSnapshotReturns() {
        String payload = "a string";
        StringValueCallback stringValueCallback = new StringValueCallback();

        FirebaseDatabase firebaseDatabase = FakeFirebase.databaseFor(payload);
        DatabaseReference reference = firebaseDatabase.getReference();

        FirebaseSingleEventListener.listen(reference, stringValueCallback, String.class);

        assertThat(stringValueCallback.receivedValue).isEqualTo(payload);
    }

    private static class StringValueCallback implements ValueCallback<String> {
        String receivedValue;

        @Override
        public void onValueRetrieved(String value) {
            receivedValue = value;
        }
    }
}
