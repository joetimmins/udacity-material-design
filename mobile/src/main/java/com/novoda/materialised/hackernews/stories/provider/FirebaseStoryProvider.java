package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.Single;
import kotlin.jvm.functions.Function1;

final class FirebaseStoryProvider implements StoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public Observable<Story> readItems(@NotNull List<Integer> ids) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("v0").child("item");
        final Function1<DataSnapshot, Story> converter = new Function1<DataSnapshot, Story>() {
            @Override
            public Story invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Story.class);
            }
        };

        Observable<Story> result = Observable.empty();
        List<Single<Story>> storySingles = new ArrayList<>(ids.size());
        for (Integer id : ids) {
            DatabaseReference childReference = databaseReference.child(Integer.toString(id));
            storySingles.add(FirebaseSingleEventListener.listen(childReference, converter));
        }
        for (int i = 0; i < storySingles.size(); i++) {
            result = result.mergeWith(storySingles.get(i).toObservable());
        }
        return result;
//
//        return Observable.fromIterable(ids)
//                .map(new Function<Integer, DatabaseReference>() {
//                    @Override
//                    public DatabaseReference apply(@NonNull Integer integer) throws Exception {
//                        return databaseReference.child(Integer.toString(integer));
//                    }
//                })
//                .map(new Function<DatabaseReference, Single<Story>>() {
//                    @Override
//                    public Single<Story> apply(@NonNull DatabaseReference reference) throws Exception {
//                        return FirebaseSingleEventListener.listen(reference, converter);
//                    }
//                })
//                .concatMap(new Function<Single<Story>, Observable<Story>>() {
//                    @Override
//                    public Observable<Story> apply(@NonNull Single<Story> storySingle) throws Exception {
//                        return storySingle.toObservable();
//                    }
//                });

    }
}
