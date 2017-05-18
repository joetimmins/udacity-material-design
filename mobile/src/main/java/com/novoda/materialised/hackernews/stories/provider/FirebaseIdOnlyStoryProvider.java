package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

final class FirebaseIdOnlyStoryProvider implements IdOnlyStoryProvider {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseIdOnlyStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void readStoryIds(@NotNull Section section, @NotNull final ValueCallback<List<Story>> callback) {
        DatabaseReference reference = firebaseDatabase.getReference("v0").child(section.getId());

        Function<DataSnapshot, List<Long>> converter = new Function<DataSnapshot, List<Long>>() {
            @Override
            public List<Long> apply(@NonNull DataSnapshot dataSnapshot) throws Exception {
                //noinspection unchecked - we know it's a list of long because the docs tell us so
                return (List<Long>) dataSnapshot.getValue();
            }
        };
        ValueCallback<List<Long>> idListCallback = new ValueCallback<List<Long>>() {
            @Override
            public void onValueRetrieved(List<Long> idList) {
                List<Story> idOnlyStories = new ArrayList<>(idList.size());
                for (Long id : idList) {
                    Story idOnlyStory = Story.IdOnly.buildFor(id.intValue());
                    idOnlyStories.add(idOnlyStory);
                }
                callback.onValueRetrieved(idOnlyStories);
            }
        };
        FirebaseSingleEventListener.listen(reference, converter, idListCallback, new ArrayList<Long>());
    }
}
