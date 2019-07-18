package com.novoda.materialised.hackernews

fun <T> (() -> T?).onNullOrExceptionReturn(defaultValue: T): T = try {
    this()
} catch (e: Throwable) {
    null
} ?: defaultValue

