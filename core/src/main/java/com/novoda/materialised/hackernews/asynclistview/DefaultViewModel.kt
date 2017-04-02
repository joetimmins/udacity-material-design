package com.novoda.materialised.hackernews.asynclistview


data class DefaultViewModel<T>(
        val viewData: T,
        val viewBehaviour: ClickListener<T>
) {
}
