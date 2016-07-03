package com.novoda.materialised.hackernews

interface ClickListener<T> {
    fun onClick(data: T)
}

class NoOpClickListener<T> : ClickListener<T> {
    override fun onClick(data: T) {
        // No-op
    }
}
