package com.novoda.materialised.firebase;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public enum FirebaseSingleton {

    INSTANCE;

    private FirebaseApp firebaseApp;

    public FirebaseApp getFirebaseApp(Context context) {
        initialiseFirebaseAppIfNecessary(context);
        return firebaseApp;
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
