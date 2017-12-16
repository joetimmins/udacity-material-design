package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
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
                return null;
            }
        };

        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final SingleEmitter<T> emitter) throws Exception {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        T value = dataSnapshot.getValue(returnClass);
                        emitter.onSuccess(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                });
            }
        });
    }

    <T> Single<List<T>> singleListOf(Class<T> elementClass) {
        return Single.create(new SingleOnSubscribe<List<T>>() {
            @Override
            public void subscribe(final SingleEmitter<List<T>> emitter) throws Exception {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<T> list = (List<T>) dataSnapshot.getValue();
                        emitter.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                });
            }
        });
    }
}
