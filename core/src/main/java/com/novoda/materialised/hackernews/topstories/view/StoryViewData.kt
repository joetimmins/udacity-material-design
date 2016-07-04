package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.generics.ViewData
import java.util.*

data class StoryViewData(
        val by: String = "",
        val commentIds: List<Int> = java.util.Arrays.asList(0),
        override val id: Int = 0,
        val score: Int = 0,
        val title: String = "",
        val url: String = ""
) : com.novoda.materialised.hackernews.generics.ViewData {}
