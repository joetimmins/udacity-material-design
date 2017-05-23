package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(context);
        StoryObservableProvider storyObservableProvider = new FirebaseStoryObservableProvider(firebaseDatabase);
        return new StoryProvider(storyObservableProvider);
    }

    public static IdOnlyStoryProvider newStoryIdProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(context);
        StoryIdProvider storyIdProvider = new FirebaseStoryIdProvider(firebaseDatabase);
        return new IdOnlyStoryProvider(storyIdProvider);
    }

}
