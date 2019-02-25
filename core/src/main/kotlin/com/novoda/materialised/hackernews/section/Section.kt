package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.ViewData

enum class Section(
    override val id: String,
    val userFacingName: String
) : ViewData<String> {
    TOP_STORIES("topstories", "Top Stories"),
    NEW("newstories", "New"),
    BEST("beststories", "Best")
}
