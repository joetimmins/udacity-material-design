package com.novoda.materialised.hackernews.stories.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.section.Section;

import java.util.List;

import org.jetbrains.annotations.NotNull;

final class FirebaseStoryIdDatabase implements StoryIdDatabase {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStoryIdDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void readStoryIds(@NotNull Section section, @NotNull final ValueCallback<? super List<Long>> callback) {
        firebaseDatabase.getReference("v0").child(section.getId()).addListenerForSingleValueEvent(
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
