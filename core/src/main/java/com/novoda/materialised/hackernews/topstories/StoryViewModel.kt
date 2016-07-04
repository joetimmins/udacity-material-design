package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.generics.ClickListener
import com.novoda.materialised.hackernews.generics.NoOpClickListener
import com.novoda.materialised.hackernews.generics.ViewModel
import java.util.*

data class StoryViewModel(
        val by: String = "",
        val commentIds: List<Int> = Arrays.asList(0),
        override val id: Int = 0,
        val score: Int = 0,
        val title: String = "",
        val url: String = "",
        override val clickListener: ClickListener<StoryViewModel> = NoOpClickListener()
) : ViewModel<StoryViewModel> {}
