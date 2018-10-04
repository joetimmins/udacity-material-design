package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.novoda.remotedb.Metadata;
import com.novoda.remotedb.Node;
import com.novoda.remotedb.NodeProvider;
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
        NodeProvider nodeProvider = NodeProvider.Companion.obtain(context, METADATA);
        Structure structure = new Structure("v0", "item");
        Node node = nodeProvider.node(structure);
        return new StoryProvider(from(node));
    }

    public static StoryIdProvider newStoryIdProvider(Context context) {
        NodeProvider nodeProvider = NodeProvider.Companion.obtain(context, METADATA);
        Structure structure = new Structure("v0");
        Node node = nodeProvider.node(structure);
        return new StoryIdProvider(from(node));
    }

    private static RemoteDatabaseNode from(Node node) {
        return new RemoteDatabaseNode() {
            @NotNull
            @Override
            public RemoteDatabaseNode child(@NotNull String id) {
                return from(node.child(id));
            }

            @NotNull
            @Override
            public <T> Single<T> singleValueOf(@NotNull Class<T> returnClass) {
                return node.singleValueOf(returnClass);
            }

            @NotNull
            @Override
            public <T> Single<List<T>> singleListOf(@NotNull Class<T> returnClass) {
                return node.singleListOf(returnClass);
            }
        };
    }
}
