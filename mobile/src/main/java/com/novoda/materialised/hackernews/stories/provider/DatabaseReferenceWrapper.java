package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
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
        Function1<DataSnapshot, T> singleValueConverter = new Function1<DataSnapshot, T>() {
            @Override
            public T invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(returnClass);
            }
        };

        return dataConvertedWith(singleValueConverter);
    }

    @Override
    public <T> Single<List<T>> singleListOf(@NotNull final Class<T> returnClass) {
        Function1<DataSnapshot, List<T>> listConverter = new Function1<DataSnapshot, List<T>>() {
            @Override
            public List<T> invoke(DataSnapshot dataSnapshot) {
                return ((List<T>) dataSnapshot.getValue());
            }
        };

        return dataConvertedWith(listConverter);
    }

    @NotNull
    private <T> Single<T> dataConvertedWith(final Function1<DataSnapshot, T> converter) {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<T> emitter) throws Exception {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        T convertedDataSnapshot = converter.invoke(dataSnapshot);
                        emitter.onSuccess(convertedDataSnapshot);
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
