package com.novoda.materialised.hackernews.stories.provider;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

enum FirebaseSingleton {

    INSTANCE;

    private FirebaseApp firebaseApp;

    FirebaseDatabase getFirebaseDatabase(Context context) {
        initialiseFirebaseAppIfNecessary(context);
        return FirebaseDatabase.getInstance(firebaseApp);
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
