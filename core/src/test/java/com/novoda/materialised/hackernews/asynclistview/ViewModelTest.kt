package com.novoda.materialised.hackernews.asynclistview

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class ViewModelTest {
    private val VIEW_ID = 4

    @Test
    fun viewModelInvokesOnClickOnViewBehaviour_whenItsOwnOnClickMethodIsInvoked() {
        val intViewData = object : ViewData<Int> {
            override val id: Int
                get() = VIEW_ID
        }

        var actualViewId = 0

        val intViewModel = ViewModel(intViewData, { viewData -> actualViewId = viewData.id })

        intViewModel.onClick()

        assertThat(actualViewId).isEqualTo(VIEW_ID)
    }
}
