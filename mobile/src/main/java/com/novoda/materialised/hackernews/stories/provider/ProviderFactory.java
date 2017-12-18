package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.obtainInstance(context);
        RemoteDatabase remoteDatabase = new FirebaseDatabaseWrapper(firebaseDatabase);
        return new StoryProvider(remoteDatabase);
    }

    public static IdOnlyStoryProvider newIdOnlyStoryProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.obtainInstance(context);
        RemoteDatabase remoteDatabase = new FirebaseDatabaseWrapper(firebaseDatabase);
        return new IdOnlyStoryProvider(remoteDatabase);
    }

}
