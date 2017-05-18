package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

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
            Function<DataSnapshot, Story> converter = new Function<DataSnapshot, Story>() {
                @Override
                public Story apply(@NonNull DataSnapshot dataSnapshot) throws Exception {
                    return dataSnapshot.getValue(Story.class);
                }
            };
            FirebaseSingleEventListener.listen(item, converter, valueCallback, new Story());
        }
    }
}
