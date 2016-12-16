package com.novoda.materialised.hackernews.asynclistview

interface ClickListener<in T> {
    fun onClick(data: T)
}

enum class NoOpClickListener : ClickListener<Any> {

    INSTANCE {
        override fun onClick(data: Any) {
            // No-op
        }
    };
}
