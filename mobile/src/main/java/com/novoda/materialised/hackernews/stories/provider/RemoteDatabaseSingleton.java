package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.remotedb.RemoteDatabase;

enum RemoteDatabaseSingleton {

    INSTANCE;

    private FirebaseApp firebaseApp;

    RemoteDatabase obtainInstance(Context context) {
        initialiseFirebaseAppIfNecessary(context);
        FirebaseDatabase instance = FirebaseDatabase.getInstance(firebaseApp);
        return new FirebaseDatabaseWrapper(instance);
    }

    private void initialiseFirebaseAppIfNecessary(Context context) {
        if (firebaseApp == null) {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setApplicationId("com.novoda.materialised") // build() throws an exception if this isn't set
                    .setDatabaseUrl("https://hacker-news.firebaseio.com")
                    .build();
            firebaseApp = FirebaseApp.initializeApp(context.getApplicationContext(), firebaseOptions, "Materialised");
        }
    }

}
