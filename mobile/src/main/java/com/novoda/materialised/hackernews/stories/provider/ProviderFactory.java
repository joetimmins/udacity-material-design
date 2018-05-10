package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNode;
import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNodeProvider;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        RemoteDatabaseNode remoteDatabase = RemoteDatabaseNodeProvider.INSTANCE.obtainNode(context, "v0", "item");
        return new StoryProvider(remoteDatabase);
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        RemoteDatabaseNode remoteDatabase = RemoteDatabaseNodeProvider.INSTANCE.obtainNode(context, "v0");
        return new StoryIdProvider(remoteDatabase);
    }

}
