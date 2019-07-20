package com.novoda.materialised.hackernews.asynclistview

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

private const val SOME_ID = 4

class UiModelTest {

    @Test
    fun viewModelInvokesGivenBehaviour() {
        val dummyData = DummyData(SOME_ID)

        var actualViewId = 0

        val dummyUiState = UiModel({ viewData -> actualViewId = viewData.id }, dummyData)

        dummyUiState.invokeBehaviour()

        assertThat(actualViewId).isEqualTo(SOME_ID)
    }
}

private data class DummyData(val id: Int)
