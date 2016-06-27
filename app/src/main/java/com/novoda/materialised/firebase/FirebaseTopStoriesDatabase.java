package com.novoda.materialised.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.topstories.TopStoriesDatabase;
import com.novoda.materialised.hackernews.ValueCallback;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class FirebaseTopStoriesDatabase implements TopStoriesDatabase {
    private final FirebaseDatabase firebaseDatabase;

    public FirebaseTopStoriesDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void readAll(@NotNull final ValueCallback<List<Long>> callback) {
        firebaseDatabase.getReference("v0").child("topstories").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Long> value = (List<Long>) dataSnapshot.getValue();
                        callback.onValueRetrieved(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}
