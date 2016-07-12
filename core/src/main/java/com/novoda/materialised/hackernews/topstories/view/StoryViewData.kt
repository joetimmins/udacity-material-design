package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.asynclistview.ViewData

data class StoryViewData(
        val by: String = "",
        val commentIds: List<Int> = java.util.Arrays.asList(0),
        override val id: Int = 0,
        val score: Int = 0,
        val title: String = "",
        val url: String = "",
        val submittedFrom: String = "news.ycombinator.com"
) : ViewData {}
