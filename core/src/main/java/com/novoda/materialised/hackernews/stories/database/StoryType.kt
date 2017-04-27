package com.novoda.materialised.hackernews.stories.database

import com.novoda.materialised.hackernews.asynclistview.ViewData


enum class StoryType(
        override val id: String,
        val userFacingName: String
) : ViewData<String> {
    TOP_STORIES("topstories", "Top Stories"),
    NEW("newstories", "New"),
    BEST("beststories", "Best")
}
