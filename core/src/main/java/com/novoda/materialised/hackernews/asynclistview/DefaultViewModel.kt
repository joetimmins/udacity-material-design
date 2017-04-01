package com.novoda.materialised.hackernews.asynclistview


data class DefaultViewModel<T>(
        private val viewData: T,
        private val viewBehaviour: ClickListener<T>
) {
}
