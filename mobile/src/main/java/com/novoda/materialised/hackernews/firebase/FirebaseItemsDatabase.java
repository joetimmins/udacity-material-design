package com.novoda.materialised.hackernews.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.topstories.database.Story;
import com.novoda.materialised.hackernews.topstories.database.ValueCallback;

import java.util.List;

import org.jetbrains.annotations.NotNull;

final class FirebaseItemsDatabase implements ItemsDatabase {
    private final FirebaseDatabase firebaseDatabase;

    FirebaseItemsDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void readItems(@NotNull List<Integer> ids, @NotNull final ValueCallback<? super Story> valueCallback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("v0").child("item");

        for (final Integer id : ids) {
            DatabaseReference item = databaseReference.child(Integer.toString(id));
            item.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Story value = dataSnapshot.getValue(Story.class);
                    if (value != null) {
                        valueCallback.onValueRetrieved(value);
                    } else {
                        Log.d("TAG", "data snapshot had no value");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
