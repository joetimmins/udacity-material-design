package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        return new FirebaseStoryProvider(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

    public static IdOnlyStoryProvider newStoryIdProvider(Context context) {
        return new FirebaseIdOnlyStoryProvider(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

}
