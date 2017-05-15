package com.novoda.materialised.hackernews

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

private val defaultValue = 23

class NullHandlerKtTest {

    @Test
    fun returnsDefaultValue_WhenFunctionReturnsNull() {
        val result = handleNullable({ null }, defaultValue)

        assertThat(result).isEqualTo(defaultValue)
    }

    @Test
    fun returnsDefaultValue_WhenFunctionThrowsException() {
        val result = handleNullable({ throw RuntimeException() }, defaultValue)

        assertThat(result).isEqualTo(defaultValue)
    }

    @Test
    fun returnsResultOFunction_WhenNotNull() {
        val expected = 12
        val result = handleNullable({ expected }, defaultValue)
        assertThat(result).isEqualTo(expected)
    }

}
