package com.novoda.remotedb

import android.content.Context
import android.support.annotation.MainThread
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase

object RemoteDatabaseProvider {

    private lateinit var firebaseApp: FirebaseApp

    fun obtainNode(context: Context, remoteDatabaseStructure: RemoteDatabaseStructure): RemoteDatabase {
        initialiseFirebaseAppIfNecessary(context)
        val instance = FirebaseDatabase.getInstance(firebaseApp)
        var reference = instance.getReference(remoteDatabaseStructure.firstChildId)
        for (childId in remoteDatabaseStructure.childIds) {
            reference = reference.child(childId)
        }
        return RemoteDatabase(reference)
    }

    @MainThread
    private fun initialiseFirebaseAppIfNecessary(context: Context) {
        if (::firebaseApp.isInitialized) {
            val firebaseOptions = FirebaseOptions.Builder()
                    .setApplicationId("com.novoda.materialised") // build() throws an exception if this isn't set
                    .setDatabaseUrl("https://hacker-news.firebaseio.com")
                    .build()
            firebaseApp = FirebaseApp.initializeApp(context.applicationContext, firebaseOptions, "Materialised")
        }
    }

}
