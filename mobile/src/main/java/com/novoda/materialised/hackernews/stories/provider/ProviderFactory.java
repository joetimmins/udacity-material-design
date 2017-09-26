package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public final class ProviderFactory {

    public static SingleStoryProvider newSingleStoryProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(context);
        return new FirebaseSingleStoryProvider(firebaseDatabase);
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(context);
        return new FirebaseStoryIdProvider(firebaseDatabase);
    }

}
