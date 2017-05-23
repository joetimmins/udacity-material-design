package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import kotlin.jvm.functions.Function1;

class FirebaseStoryObservableProvider implements StoryObservableProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStoryObservableProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public Observable<Observable<Story>> createStoryObservables(@NotNull List<Integer> storyIds) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("v0").child("item");
        final Function1<DataSnapshot, Story> converter = new Function1<DataSnapshot, Story>() {
            @Override
            public Story invoke(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Story.class);
            }
        };

        return Observable.fromIterable(storyIds)
                .map(new Function<Integer, Observable<Story>>() {
                    @Override
                    public Observable<Story> apply(@NonNull Integer id) throws Exception {
                        DatabaseReference childReference = databaseReference.child(Integer.toString(id));
                        return FirebaseSingleEventListener.listen(childReference, converter).toObservable();
                    }
                });
    }
}
