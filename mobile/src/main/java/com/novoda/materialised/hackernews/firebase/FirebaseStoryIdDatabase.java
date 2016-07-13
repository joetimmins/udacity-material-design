package com.novoda.materialised.hackernews.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.topstories.database.StoryIdDatabase;
import com.novoda.materialised.hackernews.topstories.database.ValueCallback;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class FirebaseStoryIdDatabase implements StoryIdDatabase {
    private final FirebaseDatabase firebaseDatabase;
    private final String storyType;

    public FirebaseStoryIdDatabase(FirebaseDatabase firebaseDatabase, String storyType) {
        this.firebaseDatabase = firebaseDatabase;
        this.storyType = storyType;
    }

    @Override
    public void readTopStoriesIds(@NotNull final ValueCallback<? super List<Long>> callback) {
        firebaseDatabase.getReference("v0").child(storyType).addListenerForSingleValueEvent(
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
