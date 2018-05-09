package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNode;
import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseSingleton;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        RemoteDatabaseNode remoteDatabase = RemoteDatabaseSingleton.INSTANCE.obtainInstance(context, "v0", "items");
        return new StoryProvider(remoteDatabase);
    }

    public static StoryIdProvider newIdOnlyStoryProvider(Context context) {
        RemoteDatabaseNode remoteDatabase = RemoteDatabaseSingleton.INSTANCE.obtainInstance(context, "v0");
        return new StoryIdProvider(remoteDatabase);
    }

}
