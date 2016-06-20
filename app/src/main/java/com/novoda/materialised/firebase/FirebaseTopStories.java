package com.novoda.materialised.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.database.TopStories;
import com.novoda.materialised.hackernews.database.ValueCallback;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class FirebaseTopStories implements TopStories {
    private final FirebaseDatabase firebaseDatabase;

    public FirebaseTopStories(FirebaseDatabase firebaseApp) {
        this.firebaseDatabase = firebaseApp;
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
