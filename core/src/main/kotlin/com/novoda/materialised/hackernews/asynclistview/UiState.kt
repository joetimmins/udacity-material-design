package com.novoda.materialised.hackernews.asynclistview

data class UiState<T : UiData<Any>>(
    private val behaviour: (T) -> Unit = {},
    val data: T
) {
    fun invokeBehaviour() {
        behaviour(data)
    }

    override fun equals(other: Any?): Boolean = other is UiState<*> && other.data == data

    override fun hashCode(): Int = data.hashCode()
}
