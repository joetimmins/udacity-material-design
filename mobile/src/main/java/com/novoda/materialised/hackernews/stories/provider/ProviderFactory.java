package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.remotedb.DatabaseReferenceWrapper;
import com.novoda.remotedb.RemoteDatabaseProvider;
import com.novoda.remotedb.RemoteDatabaseStructure;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;

public final class ProviderFactory {

    public static StoryProvider newStoryProvider(Context context) {
        RemoteDatabaseStructure structure = new RemoteDatabaseStructure("v0", "item");
        DatabaseReferenceWrapper remoteDatabase = RemoteDatabaseProvider.INSTANCE.obtainNode(context, structure);
        return new StoryProvider(from(remoteDatabase));
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        RemoteDatabaseStructure structure = new RemoteDatabaseStructure("v0");
        DatabaseReferenceWrapper remoteDatabase = RemoteDatabaseProvider.INSTANCE.obtainNode(context, structure);
        return new StoryIdProvider(from(remoteDatabase));
    }

    private static RemoteDatabaseNode from(DatabaseReferenceWrapper wrapper) {
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
