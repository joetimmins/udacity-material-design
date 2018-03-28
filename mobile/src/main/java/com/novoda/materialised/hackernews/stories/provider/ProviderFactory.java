package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.materialised.hackernews.remotedb.RemoteDatabase;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        RemoteDatabase remoteDatabase = RemoteDatabaseSingleton.INSTANCE.obtainInstance(context);
        return new StoryProvider(remoteDatabase);
    }

    public static StoryIdProvider newIdOnlyStoryProvider(Context context) {
        RemoteDatabase remoteDatabase = RemoteDatabaseSingleton.INSTANCE.obtainInstance(context);
        return new StoryIdProvider(remoteDatabase);
    }

}
