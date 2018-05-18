package com.novoda.materialised.hackernews.asynclistview

@Suppress("AddVarianceModifier") // adding the variance modifier adds too much noise on the Java side
data class ViewModel<T : ViewData<Any>>(
        val viewData: T,
        private val viewBehaviour: (T) -> Unit = {}
) {
    fun invokeBehaviour() {
        viewBehaviour(viewData)
    }
}
