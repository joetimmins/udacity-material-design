package com.novoda.remotedb

// this can't be a data class because data classes can't have varargs in the constructor
class Structure(
    val firstChildId: String,
    vararg val childIds: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Structure

        if (firstChildId != other.firstChildId) return false
        if (!childIds.contentEquals(other.childIds)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstChildId.hashCode()
        result = 31 * result + childIds.contentHashCode()
        return result
    }
}
