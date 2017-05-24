package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;

final class FirebaseStoryIdProvider implements StoryIdProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStoryIdProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public Single<List<Long>> listOfStoryIds(@NotNull Section section) {
        DatabaseReference reference = firebaseDatabase.getReference("v0").child(section.getId());

        Function1<DataSnapshot, List<Long>> converter = new Function1<DataSnapshot, List<Long>>() {
            @Override
            public List<Long> invoke(DataSnapshot dataSnapshot) {
                //noinspection unchecked - we know it's a list of longs because the docs tell us so
                return (List<Long>) dataSnapshot.getValue();
            }
        };

        return FirebaseSingleEventListener.listen(reference, converter);
    }
}
