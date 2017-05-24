package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import kotlin.jvm.functions.Function1;

final class FirebaseStoryObservableProvider implements StoryObservableProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStoryObservableProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public List<Observable<Story>> createStoryObservables(@NotNull List<Integer> storyIds) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("v0").child("item");
        final Function1<DataSnapshot, Story> converter = new Function1<DataSnapshot, Story>() {
            @Override
            public Story invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Story.class);
            }
        };

        List<Observable<Story>> storyObservables = new ArrayList<>(storyIds.size());
        for (Integer storyId : storyIds) {
            DatabaseReference childReference = databaseReference.child(Integer.toString(storyId));
            Observable<Story> storyObservable = FirebaseSingleEventListener.listen(childReference, converter).toObservable();
            storyObservables.add(storyObservable);
        }
        return storyObservables;
    }
}
