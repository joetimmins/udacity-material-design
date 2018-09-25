package com.novoda.materialised.hackernews

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

private const val defaultValue = 236

class NullHandlerKtTest {

    @Test
    fun returnDefaultValue_WhenFunctionReturnsNull() {
        val result = handleExceptionOrNull({ null }, defaultValue)

        assertThat(result).isEqualTo(defaultValue)
    }

    @Test
    fun returnDefaultValue_WhenFunctionThrowsException() {
        val result = handleExceptionOrNull({ throw RuntimeException() }, defaultValue)

        assertThat(result).isEqualTo(defaultValue)
    }

    @Test
    fun returnResultOFunction_WhenNotNull() {
        val expected = 12
        val result = handleExceptionOrNull({ expected }, defaultValue)
        assertThat(result).isEqualTo(expected)
    }

}
