package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;

final class FirebaseStoryIdProvider implements StoryIdProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStoryIdProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public Single<List<Long>> listOfStoryIds(@NotNull Section section) {
        DatabaseReferenceWrapper wrapper = new DatabaseReferenceWrapper(firebaseDatabase.getReference("v0")).child(section.getId());
        return wrapper.singleListOf(Long.class);
    }
}
