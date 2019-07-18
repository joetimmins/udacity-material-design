package com.novoda.materialised.hackernews

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

private const val defaultValue = 236

class NullHandlerKtTest {

    @Test
    fun returnDefaultValue_WhenFunctionReturnsNull() {
        val result = { null }.onNullOrExceptionReturn(defaultValue)

        assertThat(result).isEqualTo(defaultValue)
    }

    @Test
    fun returnDefaultValue_WhenFunctionThrowsException() {
        val result = { throw RuntimeException() }.onNullOrExceptionReturn(defaultValue)

        assertThat(result).isEqualTo(defaultValue)
    }

    @Test
    fun returnResultOFunction_WhenNotNull() {
        val expected = 12
        val result = { expected }.onNullOrExceptionReturn(defaultValue)
        assertThat(result).isEqualTo(expected)
    }

}
