package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

final class FirebaseDatabaseWrapper implements RemoteDatabase {

    private final FirebaseDatabase firebaseDatabase;

    FirebaseDatabaseWrapper(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public RemoteDatabaseNode node(@NotNull String name) {
        return new DatabaseReferenceWrapper(firebaseDatabase.getReference(name));
    }
}
