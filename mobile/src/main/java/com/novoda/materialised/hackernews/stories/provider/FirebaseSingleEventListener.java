package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.functions.Function;
import kotlin.jvm.functions.Function0;

import static com.novoda.materialised.hackernews.NullHandlerKt.handleNullable;

final class FirebaseSingleEventListener {

    private FirebaseSingleEventListener() {
        // nah
    }

    static <T> void listen(
            DatabaseReference reference,
            final Function<DataSnapshot, T> converter,
            final ValueCallback<T> valueCallback,
            final T defaultValue
    ) {
        reference.addListenerForSingleValueEvent(new SingleValueEventListener<>(converter, valueCallback, defaultValue));
    }

    private static class SingleValueEventListener<T> implements ValueEventListener {
        private final Function<DataSnapshot, T> converter;
        private final ValueCallback<T> valueCallback;
        private final T defaultValue;

        SingleValueEventListener(Function<DataSnapshot, T> converter, ValueCallback<T> valueCallback, T defaultValue) {
            this.defaultValue = defaultValue;
            this.valueCallback = valueCallback;
            this.converter = converter;
        }

        @Override
        public void onDataChange(final DataSnapshot dataSnapshot) {
            Function0<? extends T> something = getFunction0(dataSnapshot);
            T retrievedValue = handleNullable(something, defaultValue);

            valueCallback.onValueRetrieved(retrievedValue);
        }

        private Function0<? extends T> getFunction0(final DataSnapshot dataSnapshot) {
            return new Function0<T>() {
                @Override
                public T invoke() {
                    try {
                        return converter.apply(dataSnapshot);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
