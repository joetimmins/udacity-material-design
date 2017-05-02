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
        val viewBehaviour = object : ClickListener<ViewData<Int>> {
            var lastReceivedInt: Int = 0
            override fun onClick(data: ViewData<Int>) {
                lastReceivedInt = data.id
            }
        }
        val intViewModel = ViewModel(intViewData, viewBehaviour)

        intViewModel.onClick()

        assertThat(viewBehaviour.lastReceivedInt).isEqualTo(VIEW_ID)
    }
}
