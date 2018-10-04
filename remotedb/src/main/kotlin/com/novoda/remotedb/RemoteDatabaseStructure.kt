package com.novoda.remotedb

// this can't be a data class because data classes can't have varargs in the constructor
class RemoteDatabaseStructure(val firstChildId: String, vararg val childIds: String)
