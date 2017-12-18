package com.novoda.materialised.hackernews.stories.provider;

import com.novoda.materialised.hackernews.section.Section;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;

final class FirebaseStoryIdProvider implements StoryIdProvider {
    private final RemoteDatabase remoteDatabase;

    FirebaseStoryIdProvider(RemoteDatabase remoteDatabase) {
        this.remoteDatabase = remoteDatabase;
    }

    @NotNull
    @Override
    public Single<List<Long>> listOfStoryIds(@NotNull Section section) {
        RemoteDatabaseNode child = remoteDatabase.node("v0").child(section.getId());
        return child.singleList();
    }
}
