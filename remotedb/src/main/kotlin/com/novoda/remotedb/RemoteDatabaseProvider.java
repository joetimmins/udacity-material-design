package com.novoda.remotedb;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public enum RemoteDatabaseProvider {

    INSTANCE;

    private FirebaseApp firebaseApp;

    public RemoteDatabase obtainNode(Context context, RemoteDatabaseStructure remoteDatabaseStructure) {
        initialiseFirebaseAppIfNecessary(context);
        FirebaseDatabase instance = FirebaseDatabase.getInstance(firebaseApp);
        DatabaseReference reference = instance.getReference(remoteDatabaseStructure.getFirstChildId());
        for (String childId : remoteDatabaseStructure.getChildIds()) {
            reference = reference.child(childId);
        }
        return new RemoteDatabase(reference);
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