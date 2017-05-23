package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import kotlin.jvm.functions.Function1;

final class FirebaseIdOnlyStoryProvider implements IdOnlyStoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseIdOnlyStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NotNull
    @Override
    public Single<List<Story>> idOnlyStoriesFor(@NotNull Section section) {
        DatabaseReference reference = firebaseDatabase.getReference("v0").child(section.getId());

        Function1<DataSnapshot, List<Long>> converter = new Function1<DataSnapshot, List<Long>>() {
            @Override
            public List<Long> invoke(DataSnapshot dataSnapshot) {
                //noinspection unchecked - we know it's a list of longs because the docs tell us so
                return (List<Long>) dataSnapshot.getValue();
            }
        };

        Single<List<Long>> listOfStoryIds = FirebaseSingleEventListener.listen(reference, converter);

        return listOfStoryIds
                .flatMapObservable(new Function<List<Long>, Observable<Long>>() {
                    @Override
                    public Observable<Long> apply(@NonNull List<Long> longs) throws Exception {
                        return Observable.fromIterable(longs);
                    }
                })
                .map(new Function<Long, Story>() {
                    @Override
                    public Story apply(@NonNull Long rawId) throws Exception {
                        return Story.IdOnly.buildFor(rawId.intValue());
                    }
                })
                .reduce(new ArrayList<Story>(), new BiFunction<List<Story>, Story, List<Story>>() {
                    @Override
                    public List<Story> apply(@NonNull List<Story> stories, @NonNull Story story) throws Exception {
                        stories.add(story);
                        return stories;
                    }
                });
    }
}
