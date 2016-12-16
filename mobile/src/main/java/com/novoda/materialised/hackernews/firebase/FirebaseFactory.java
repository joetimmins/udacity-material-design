package com.novoda.materialised.hackernews.firebase;

import android.content.Context;

import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.topstories.database.StoryIdDatabase;

public final class FirebaseFactory {

    public static ItemsDatabase newItemsDatabase(Context context) {
        return new FirebaseItemsDatabase(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

    public static StoryIdDatabase newStoryIdDatabase(Context context) {
        return new FirebaseStoryIdDatabase(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

}
