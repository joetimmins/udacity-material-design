package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public final class ProviderFactory {

    public static SingleStoryProvider newSingleStoryProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.obtainInstance(context);
        RemoteDatabase remoteDatabase = new FirebaseDatabaseWrapper(firebaseDatabase);
        return new FirebaseSingleStoryProvider(remoteDatabase);
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.obtainInstance(context);
        RemoteDatabase remoteDatabase = new FirebaseDatabaseWrapper(firebaseDatabase);
        return new FirebaseStoryIdProvider(remoteDatabase);
    }

}
