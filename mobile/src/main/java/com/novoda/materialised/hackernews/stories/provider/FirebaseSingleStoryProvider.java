package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;

final class FirebaseSingleStoryProvider implements SingleStoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseSingleStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public Single<Story> obtainStory(int storyId) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("v0").child("item");
        final Function1<DataSnapshot, Story> converter = new Function1<DataSnapshot, Story>() {
            @Override
            public Story invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Story.class);
            }
        };

        DatabaseReference childReference = databaseReference.child(Integer.toString(storyId));
        return FirebaseSingleEventListener.listen(childReference, converter);
    }
}
