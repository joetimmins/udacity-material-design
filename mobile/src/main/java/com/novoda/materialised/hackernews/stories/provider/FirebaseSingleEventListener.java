package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import kotlin.jvm.functions.Function1;

final class FirebaseSingleEventListener {

    private FirebaseSingleEventListener() {
        // nah
    }

    @NotNull
    static <T> Single<T> listen(final DatabaseReference reference, final Function1<DataSnapshot, T> converter) {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<T> emitter) throws Exception {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
