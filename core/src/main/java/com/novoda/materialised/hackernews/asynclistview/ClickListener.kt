package com.novoda.materialised.hackernews.asynclistview

interface ClickListener<in T> {
    fun onClick(data: T)
}

class NoOpClickListener<in T> : ClickListener<T> {
    override fun onClick(data: T) {
        // No-op
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is NoOpClickListener<*> // this sucks, but the type gets erased, so there's nothing else we can do
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}