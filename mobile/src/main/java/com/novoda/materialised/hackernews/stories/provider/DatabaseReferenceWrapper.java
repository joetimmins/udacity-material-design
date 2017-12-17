package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
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

        return dataConvertedWith(singleValueConverter);
    }

    <T> Single<List<T>> singleListOf(Class<T> elementClass) {
        Function1<DataSnapshot, List<T>> listConverter = new Function1<DataSnapshot, List<T>>() {
            @Override
            public List<T> invoke(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<T>> typeIndicator = new GenericTypeIndicator<List<T>>() {
                };
                return dataSnapshot.getValue(typeIndicator);
            }
        };

        return dataConvertedWith(listConverter);
    }

    <T> Single<T> singleValueOf(final GenericTypeIndicator<T> indicator) {
        Function1<DataSnapshot, T> converter = new Function1<DataSnapshot, T>() {
            @Override
            public T invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(indicator);
            }
        };
        return dataConvertedWith(converter);
    }

    <T> Type[] something(WorkaroundForTypeErasure<T> workaroundForTypeErasure) {
        Type genericSuperclass = workaroundForTypeErasure.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[0];
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

    static class WorkaroundForTypeErasure<T> {
        WorkaroundForTypeErasure() {
        }
    }
}
