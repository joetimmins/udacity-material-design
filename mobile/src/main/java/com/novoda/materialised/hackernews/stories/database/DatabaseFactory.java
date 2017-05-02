package com.novoda.materialised.hackernews.stories.database;

import android.content.Context;

public final class DatabaseFactory {

    public static StoryProvider newItemsDatabase(Context context) {
        return new FirebaseStoryProvider(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

    public static StoryIdDatabase newStoryIdDatabase(Context context) {
        return new FirebaseStoryIdDatabase(FirebaseSingleton.INSTANCE.getFirebaseDatabase(context));
    }

}
