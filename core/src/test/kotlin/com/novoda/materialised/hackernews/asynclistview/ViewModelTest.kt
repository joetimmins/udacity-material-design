package com.novoda.materialised.hackernews.asynclistview

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

private const val viewId = 4

class ViewModelTest {

    @Test
    fun viewModelInvokesGivenBehaviour() {
        val intViewData = object : ViewData<Int> {
            override val id: Int
                get() = viewId
        }

        var actualViewId = 0

        val intViewModel = ViewModel({ viewData -> actualViewId = viewData.id }, intViewData)

        intViewModel.invokeBehaviour()

        assertThat(actualViewId).isEqualTo(viewId)
    }
}
