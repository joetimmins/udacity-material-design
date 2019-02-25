package com.novoda.remotedb

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import io.reactivex.Single

class Node internal constructor(private val databaseReference: DatabaseReference) {

    fun child(nodeId: String): Node {
        val child = databaseReference.child(nodeId)
        return Node(child)
    }

    fun <T> singleValueOf(returnClass: Class<T>): Single<T> = databaseReference.toSingle { getValue(returnClass) }

    fun <T> singleListOf(returnClass: Class<T>): Single<List<T>> = databaseReference.toSingle { value as List<T> }

    private fun <T> DatabaseReference.toSingle(toReturnType: DataSnapshot.() -> T?) =
        Single.create<T> { emitter ->
            addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(data: DataSnapshot) =
                        run { data.toReturnType()?.let { emitter.onSuccess(it) } ?: Unit }

                    override fun onCancelled(error: DatabaseError) =
                        run { emitter.onError(error.toException()) }
                }
            )
        }
}
