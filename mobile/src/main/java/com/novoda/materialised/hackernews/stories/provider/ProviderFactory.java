package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNode;
import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNodeProvider;
import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseStructure;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        RemoteDatabaseStructure structure = new RemoteDatabaseStructure("v0", "item");
        RemoteDatabaseNode remoteDatabase = RemoteDatabaseNodeProvider.INSTANCE.obtainNode(context, structure);
        return new StoryProvider(remoteDatabase);
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        RemoteDatabaseStructure structure = new RemoteDatabaseStructure("v0");
        RemoteDatabaseNode remoteDatabase = RemoteDatabaseNodeProvider.INSTANCE.obtainNode(context, structure);
        return new StoryIdProvider(remoteDatabase);
    }

}
