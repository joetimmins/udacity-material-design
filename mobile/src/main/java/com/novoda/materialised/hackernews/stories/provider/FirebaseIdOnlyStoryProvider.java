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
import io.reactivex.functions.Function;
import kotlin.jvm.functions.Function1;

final class FirebaseIdOnlyStoryProvider implements IdOnlyStoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseIdOnlyStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public Observable<List<Story>> readStoryIds(@NotNull Section section, @NotNull final ValueCallback<List<Story>> callback) {
        DatabaseReference reference = firebaseDatabase.getReference("v0").child(section.getId());

        Function1<DataSnapshot, List<Long>> converter = new Function1<DataSnapshot, List<Long>>() {
            @Override
            public List<Long> invoke(DataSnapshot dataSnapshot) {
                //noinspection unchecked - we know it's a list of longs because the docs tell us so
                return (List<Long>) dataSnapshot.getValue();
            }
        };

        Function<List<Long>, List<Story>> buildIdOnlyStories = new Function<List<Long>, List<Story>>() {
            @Override
            public List<Story> apply(@NonNull List<Long> longs) throws Exception {
                List<Story> idOnlyStories = new ArrayList<>(longs.size());
                for (Long id : longs) {
                    Story idOnlyStory = Story.IdOnly.buildFor(id.intValue());
                    idOnlyStories.add(idOnlyStory);
                }
                return idOnlyStories;
            }
        };

        Single<List<Long>> listOfStoryIds = FirebaseSingleEventListener.listen(reference, converter);
        return listOfStoryIds.map(buildIdOnlyStories).toObservable();
    }
}
