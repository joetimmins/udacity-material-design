package com.novoda.materialised.hackernews

fun <T> handleNullable(mightBeNullOrThrowAnException: () -> T?, defaultValue: T): T {

    val mightBeNull = handleExceptions(mightBeNullOrThrowAnException)

    return mightBeNull ?: defaultValue
}

private fun <T> handleExceptions(mightThrowAnException: () -> T?): T? {
    return try {
        mightThrowAnException()
    } catch (e: Throwable) {
        null
    }
}
