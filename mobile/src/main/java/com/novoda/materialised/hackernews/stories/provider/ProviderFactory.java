package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        return new FirebaseStoryProvider(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

    public static IdOnlyStoryProvider newStoryIdProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(context);
        FirebaseStoryIdProvider firebaseStoryIdProvider = new FirebaseStoryIdProvider(firebaseDatabase);
        return new IdOnlyStoryProvider(firebaseStoryIdProvider);
    }

}
