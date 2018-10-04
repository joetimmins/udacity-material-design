package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.remotedb.Metadata;
import com.novoda.remotedb.RemoteDatabase;
import com.novoda.remotedb.RemoteDatabaseProvider;
import com.novoda.remotedb.Structure;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;

public final class ProviderFactory {

    private static String APPLICATION_ID = "com.novoda.materialised";
    private static String APPLICATION_NAME = "Material HN";
    private static String DATABASE_URL = "https://hacker-news.firebaseio.com";
    private static Metadata METADATA = new Metadata(APPLICATION_ID, APPLICATION_NAME, DATABASE_URL);

    public static StoryProvider newStoryProvider(Context context) {
        RemoteDatabaseProvider remoteDatabaseProvider = RemoteDatabaseProvider.Companion.obtain(context, METADATA);
        Structure structure = new Structure("v0", "item");
        RemoteDatabase remoteDatabase = remoteDatabaseProvider.node(structure);
        return new StoryProvider(from(remoteDatabase));
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        RemoteDatabaseProvider remoteDatabaseProvider = RemoteDatabaseProvider.Companion.obtain(context, METADATA);
        Structure structure = new Structure("v0");
        RemoteDatabase remoteDatabase = remoteDatabaseProvider.node(structure);
        return new StoryIdProvider(from(remoteDatabase));
    }

    private static RemoteDatabaseNode from(RemoteDatabase wrapper) {
        return new RemoteDatabaseNode() {
            @NotNull
            @Override
            public RemoteDatabaseNode child(@NotNull String id) {
                return from(wrapper.child(id));
            }

            @NotNull
            @Override
            public <T> Single<T> singleValueOf(@NotNull Class<T> returnClass) {
                return wrapper.singleValueOf(returnClass);
            }

            @NotNull
            @Override
            public <T> Single<List<T>> singleListOf(@NotNull Class<T> returnClass) {
                return wrapper.singleListOf(returnClass);
            }
        };
    }
}
