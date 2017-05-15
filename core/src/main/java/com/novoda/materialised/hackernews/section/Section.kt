package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.asynclistview.ViewData

enum class Section(
        override val id: String,
        val userFacingName: String,
        val isDefault: Boolean
) : ViewData<String> {
    TOP_STORIES("topstories", "Top Stories", true),
    NEW("newstories", "New", false),
    BEST("beststories", "Best", false)
}
