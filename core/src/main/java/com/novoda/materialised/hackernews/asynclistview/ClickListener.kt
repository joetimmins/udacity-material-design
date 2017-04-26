package com.novoda.materialised.hackernews.asynclistview

interface ClickListener<in T> {
    fun onClick(data: T)

    object noOpClickListener : ClickListener<Any> {
        override fun onClick(data: Any) {
            // No-op
        }
    }
}
