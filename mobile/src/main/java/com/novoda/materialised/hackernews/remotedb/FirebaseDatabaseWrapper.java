package com.novoda.materialised.hackernews.remotedb;

import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

final class FirebaseDatabaseWrapper implements RemoteDatabase {

    private final FirebaseDatabase firebaseDatabase;

    FirebaseDatabaseWrapper(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public RemoteDatabaseNode child(@NotNull String name) {
        return new DatabaseReferenceWrapper(firebaseDatabase.getReference(name));
    }
}
