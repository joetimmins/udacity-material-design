package com.novoda.materialised.hackernews.asynclistview

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

private const val viewId = 4

class UiStateTest {

    @Test
    fun viewModelInvokesGivenBehaviour() {
        val intViewData = object : UiData<Int> {
            override val id: Int
                get() = viewId
        }

        var actualViewId = 0

        val intViewModel = UiState({ viewData -> actualViewId = viewData.id }, intViewData)

        intViewModel.invokeBehaviour()

        assertThat(actualViewId).isEqualTo(viewId)
    }
}
