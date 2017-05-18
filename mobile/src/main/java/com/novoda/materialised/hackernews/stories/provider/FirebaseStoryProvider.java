package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function1;

final class FirebaseStoryProvider implements StoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void readItems(@NotNull List<Integer> ids, @NotNull final ValueCallback<Story> valueCallback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("v0").child("item");

        for (final Integer id : ids) {
            DatabaseReference item = databaseReference.child(Integer.toString(id));
            Function1<DataSnapshot, Story> converter = new Function1<DataSnapshot, Story>() {
                @Override
                public Story invoke(DataSnapshot dataSnapshot) {
                    return dataSnapshot.getValue(Story.class);
                }
            };
            FirebaseSingleEventListener.listen(item, converter);
        }
    }
}
