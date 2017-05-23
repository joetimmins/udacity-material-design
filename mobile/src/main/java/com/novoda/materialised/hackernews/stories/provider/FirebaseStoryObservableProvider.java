package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;

class FirebaseStoryObservableProvider {
    private final FirebaseDatabase firebaseDatabase;

    public FirebaseStoryObservableProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }
}
