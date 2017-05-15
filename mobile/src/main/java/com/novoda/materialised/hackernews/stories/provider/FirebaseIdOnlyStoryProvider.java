package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.section.Section;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

final class FirebaseIdOnlyStoryProvider implements IdOnlyStoryProvider {
    private static final Story BLANK_STORY = new Story();

    private final FirebaseDatabase firebaseDatabase;

    FirebaseIdOnlyStoryProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void readStoryIds(@NotNull Section section, @NotNull final ValueCallback<? super List<Story>> callback) {
        firebaseDatabase.getReference("v0").child(section.getId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Long> idList = (List<Long>) dataSnapshot.getValue();
                        List<Story> idOnlyStories = new ArrayList<>(idList.size());
                        for (Long id : idList) {
                            Story idOnlyStory = createIdOnlyStoryUsing(id);
                            idOnlyStories.add(idOnlyStory);
                        }
                        callback.onValueRetrieved(idOnlyStories);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private Story createIdOnlyStoryUsing(Long id) {
        return new Story(
                BLANK_STORY.getBy(), BLANK_STORY.getDescendants(), id.intValue(), BLANK_STORY.getKids(),
                BLANK_STORY.getScore(), BLANK_STORY.getTime(), BLANK_STORY.getTitle(), BLANK_STORY.getType(), BLANK_STORY.getUrl()
        );
    }
}
