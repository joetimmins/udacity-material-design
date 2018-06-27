package com.novoda.materialised.hackernews.stories.view

import com.novoda.materialised.hackernews.asynclistview.ViewData


sealed class StoryViewData : ViewData<Int> {
    data class JustAnId(override val id: Int) : StoryViewData()
    data class FullyPopulated(val by: String = "",
                              val commentIds: List<Int> = emptyList(),
                              override val id: Int = 0,
                              val score: Int = 0,
                              val title: String = "",
                              val url: String = "") : StoryViewData()
}
