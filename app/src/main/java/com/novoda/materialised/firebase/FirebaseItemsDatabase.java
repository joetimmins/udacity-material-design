package com.novoda.materialised.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.items.Story;
import com.novoda.materialised.hackernews.items.StoryViewModel;
import com.novoda.materialised.hackernews.items.ItemsDatabase;
import com.novoda.materialised.hackernews.ValueCallback;

import org.jetbrains.annotations.NotNull;

public final class FirebaseItemsDatabase implements ItemsDatabase {
    private FirebaseDatabase firebaseDatabase;

    public FirebaseItemsDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void readItem(int id, @NotNull final ValueCallback<StoryViewModel> valueCallback) {
        DatabaseReference item = firebaseDatabase.getReference("v0").child("item").child(Integer.toString(id));

        item.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Story value = dataSnapshot.getValue(Story.class);
                if (value != null) {
                    valueCallback.onValueRetrieved(convertStoryToViewModel(value));
                } else {
                    Log.d("TAG", "data snapshot had no value");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private StoryViewModel convertStoryToViewModel(Story story) {
        return new StoryViewModel(story.getBy(), story.getKids(), story.getScore(), story.getTitle(), story.getUrl());
    }
}
