package com.novoda.materialised.hackernews.stories.database;

import android.content.Context;

public final class DatabaseFactory {

    public static ItemsDatabase newItemsDatabase(Context context) {
        return new FirebaseItemsDatabase(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

    public static StoryIdDatabase newStoryIdDatabase(Context context) {
        return new FirebaseStoryIdDatabase(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

}
