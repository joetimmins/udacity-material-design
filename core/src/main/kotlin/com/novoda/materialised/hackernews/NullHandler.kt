package com.novoda.materialised.hackernews

fun <T> handleExceptionOrNull(mightBeNullOrThrowAnException: () -> T?, defaultValue: T): T = try {
    mightBeNullOrThrowAnException()
} catch (e: Throwable) {
    null
} ?: defaultValue

