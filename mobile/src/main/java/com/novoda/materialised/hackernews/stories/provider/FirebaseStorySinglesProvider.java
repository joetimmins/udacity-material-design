package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;

final class FirebaseStorySinglesProvider implements StorySinglesProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStorySinglesProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public List<Single<Story>> obtainStories(@NotNull List<Integer> storyIds) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("v0").child("item");
        final Function1<DataSnapshot, Story> converter = new Function1<DataSnapshot, Story>() {
            @Override
            public Story invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Story.class);
            }
        };

        List<Single<Story>> storyObservables = new ArrayList<>(storyIds.size());
        for (Integer storyId : storyIds) {
            DatabaseReference childReference = databaseReference.child(Integer.toString(storyId));
            Single<Story> storyObservable = FirebaseSingleEventListener.listen(childReference, converter);
            storyObservables.add(storyObservable);
        }
        return storyObservables;
    }
}
