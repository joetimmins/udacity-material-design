package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.section.Section;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

final class FirebaseIdOnlyStoryProvider implements IdOnlyStoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseIdOnlyStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void idOnlyStoriesFor(@NotNull Section section, @NotNull final ValueCallback<? super List<Story>> callback) {
        firebaseDatabase.getReference("v0").child(section.getId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Long> ids = (List<Long>) dataSnapshot.getValue();
                        List<Story> idOnlyStories = new ArrayList<>(ids.size());
                        for (Long id : ids) {
                            idOnlyStories.add(Story.IdOnly.buildFor(id.intValue()));
                        }
                        callback.onValueRetrieved(idOnlyStories);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}