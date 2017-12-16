package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;

final class FirebaseSingleStoryProvider implements SingleStoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseSingleStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public Single<Story> obtainStory(int storyId) {
        final DatabaseReferenceWrapper wrapper = new DatabaseReferenceWrapper(firebaseDatabase.getReference("v0")).child("item").child(Integer.toString(storyId));
        return wrapper.singleValueOf(Story.class);
    }
}
