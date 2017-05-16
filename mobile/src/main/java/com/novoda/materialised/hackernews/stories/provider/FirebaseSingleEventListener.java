package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

final class FirebaseSingleEventListener {

    private FirebaseSingleEventListener() {
        // nah
    }

    static <T> void listen(DatabaseReference reference, final ValueCallback<T> valueCallback, final Class<T> returnClass) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(returnClass);
                valueCallback.onValueRetrieved(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
