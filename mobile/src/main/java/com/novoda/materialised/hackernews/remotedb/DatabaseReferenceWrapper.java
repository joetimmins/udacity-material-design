package com.novoda.materialised.hackernews.remotedb;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;

final class DatabaseReferenceWrapper implements RemoteDatabaseNode {
    private final DatabaseReference databaseReference;

    DatabaseReferenceWrapper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Override
    public RemoteDatabaseNode child(@NotNull String nodeId) {
        DatabaseReference child = databaseReference.child(nodeId);
        return new DatabaseReferenceWrapper(child);
    }

    @Override
    public <T> Single<T> singleValueOf(@NotNull final Class<T> returnClass) {
        return dataConvertedWith(dataSnapshot -> dataSnapshot.getValue(returnClass));
    }

    @Override
    public <T> Single<List<T>> singleListOf(@NotNull final Class<T> returnClass) {
        return dataConvertedWith(dataSnapshot -> (List<T>) dataSnapshot.getValue());
    }

    @NotNull
    private <T> Single<T> dataConvertedWith(final Function1<DataSnapshot, T> converter) {
        return Single.create(emitter -> databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                T convertedDataSnapshot = converter.invoke(dataSnapshot);
                emitter.onSuccess(convertedDataSnapshot);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                emitter.onError(databaseError.toException());
            }
        }));
    }
}
