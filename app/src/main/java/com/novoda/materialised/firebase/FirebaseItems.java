package com.novoda.materialised.firebase;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.Story;
import com.novoda.materialised.hackernews.StoryViewModel;
import com.novoda.materialised.hackernews.database.Items;
import com.novoda.materialised.hackernews.database.ValueCallback;

import org.jetbrains.annotations.NotNull;

public final class FirebaseItems implements Items {
    private FirebaseApp firebaseApp;

    public FirebaseItems(FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
    }

    @Override
    public void readItem(int id, @NotNull final ValueCallback<StoryViewModel> valueCallback) {
        DatabaseReference item = FirebaseDatabase.getInstance(firebaseApp).getReference("v0").child("item").child(Integer.toString(id));

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
