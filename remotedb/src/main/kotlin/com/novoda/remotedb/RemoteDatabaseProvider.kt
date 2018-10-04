package com.novoda.remotedb

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase

class RemoteDatabaseProvider private constructor(private val firebaseApp: FirebaseApp) {

    fun node(structure: Structure): RemoteDatabase {
        var reference = firebaseApp.referenceFor(structure)
        structure.childIds.forEach { reference = reference.child(it) }
        return RemoteDatabase(reference)
    }

    private fun FirebaseApp.referenceFor(structure: Structure) = FirebaseDatabase.getInstance(firebaseApp).getReference(structure.firstChildId)

    companion object {

        private var lastUsedMetadata: Metadata? = null // wanted to use lateinit here too, but isInitialized doesn't work for some reason
        private lateinit var lastReturnedProvider: RemoteDatabaseProvider

        fun obtain(context: Context, metadata: Metadata): RemoteDatabaseProvider =
                when {
                    lastUsedMetadata != null && metadata == lastUsedMetadata -> lastReturnedProvider
                    else -> createNewProvider(context, metadata)
                }

        private fun createNewProvider(context: Context, metadata: Metadata): RemoteDatabaseProvider {
            val firebaseApp = FirebaseApp.initializeApp(
                    context.applicationContext,
                    FirebaseOptions.Builder().containing(metadata),
                    metadata.applicationName
            )
            lastUsedMetadata = metadata
            lastReturnedProvider = RemoteDatabaseProvider(firebaseApp)
            return lastReturnedProvider
        }

        private fun FirebaseOptions.Builder.containing(metadata: Metadata): FirebaseOptions = apply {
            setApplicationId(metadata.applicationId)
            setDatabaseUrl(metadata.databaseUrl)
        }.build()
    }
}
