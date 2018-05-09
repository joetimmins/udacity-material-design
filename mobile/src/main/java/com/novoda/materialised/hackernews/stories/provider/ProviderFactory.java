package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.materialised.hackernews.remotedb.RemoteDatabase;
import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseNode;
import com.novoda.materialised.hackernews.remotedb.RemoteDatabaseSingleton;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        RemoteDatabase remoteDatabase = RemoteDatabaseSingleton.INSTANCE.obtainInstance(context);
        RemoteDatabaseNode itemsDatabase = remoteDatabase.child("v0").child("items");
        return new StoryProvider(itemsDatabase);
    }

    public static StoryIdProvider newIdOnlyStoryProvider(Context context) {
        RemoteDatabase remoteDatabase = RemoteDatabaseSingleton.INSTANCE.obtainInstance(context);
        RemoteDatabaseNode sectionDatabase = remoteDatabase.child("v0");
        return new StoryIdProvider(sectionDatabase);
    }

}
