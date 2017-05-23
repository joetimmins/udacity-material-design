package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
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

//        Observable<Story> result = Observable.empty();
//        List<Single<Story>> storySingles = new ArrayList<>(ids.size());
//        for (Integer id : ids) {
//            DatabaseReference childReference = databaseReference.child(Integer.toString(id));
//            storySingles.add(FirebaseSingleEventListener.listen(childReference, converter));
//        }
//        for (Single<Story> storySingle : storySingles) {
//            result = result.mergeWith(storySingle.toObservable());
//        }
//        return result;
        return Observable.fromIterable(ids)
                .map(new Function<Integer, Observable<Story>>() {
                    @Override
                    public Observable<Story> apply(@NonNull Integer id) throws Exception {
                        DatabaseReference childReference = databaseReference.child(Integer.toString(id));
                        return FirebaseSingleEventListener.listen(childReference, converter).toObservable();
                    }
                })
                .reduce(new BiFunction<Observable<Story>, Observable<Story>, Observable<Story>>() {
                    @Override
                    public Observable<Story> apply(@NonNull Observable<Story> storyObservable, @NonNull Observable<Story> nextStoryObservable) throws Exception {
                        return storyObservable.mergeWith(nextStoryObservable);
                    }
                })
                .flatMapObservable(new Function<Observable<Story>, ObservableSource<? extends Story>>() {
                    @Override
                    public ObservableSource<? extends Story> apply(@NonNull Observable<Story> storyObservable) throws Exception {
                        return storyObservable;
                    }
                });
    }
}
