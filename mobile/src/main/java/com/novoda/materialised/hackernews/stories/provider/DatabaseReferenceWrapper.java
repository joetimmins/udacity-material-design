package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;

final class DatabaseReferenceWrapper {
    private final DatabaseReference databaseReference;

    DatabaseReferenceWrapper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    DatabaseReferenceWrapper child(String nodeId) {
        DatabaseReference child = databaseReference.child(nodeId);
        return new DatabaseReferenceWrapper(child);
    }

    <T> Single<T> singleValueOf(final Class<T> returnClass) {
        Function1<DataSnapshot, T> singleValueConverter = new Function1<DataSnapshot, T>() {
            @Override
            public T invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(returnClass);
            }
        };

        return FirebaseSingleEventListener.listen(databaseReference, singleValueConverter);
    }

    <T> Single<List<T>> singleListOf(Class<T> elementClass) {
        Function1<DataSnapshot, List<T>> listConverter = new Function1<DataSnapshot, List<T>>() {
            @Override
            public List<T> invoke(DataSnapshot dataSnapshot) {
                return (List<T>) dataSnapshot.getValue();
            }
        };

        return FirebaseSingleEventListener.listen(databaseReference, listConverter);
    }
}
