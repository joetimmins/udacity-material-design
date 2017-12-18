package com.novoda.materialised.hackernews.stories.provider;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;

final class FirebaseSingleStoryProvider implements SingleStoryProvider {
    private final RemoteDatabase remoteDatabase;

    FirebaseSingleStoryProvider(RemoteDatabase remoteDatabase) {
        this.remoteDatabase = remoteDatabase;
    }

    @NotNull
    @Override
    public Single<Story> obtainStory(int storyId) {
        final RemoteDatabaseNode child = remoteDatabase.node("v0").child("item").child(Integer.toString(storyId));
        return child.singleValue();
    }
}
