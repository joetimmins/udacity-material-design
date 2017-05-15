package com.novoda.materialised.hackernews.stories.database;

import android.content.Context;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        return new FirebaseStoryProvider(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        return new FirebaseStoryIdProvider(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

}
