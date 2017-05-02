package com.novoda.materialised.hackernews.section

enum class Section(
        val id: String,
        val userFacingName: String
) {
    TOP_STORIES("topstories", "Top Stories"),
    NEW("newstories", "New"),
    BEST("beststories", "Best")
}
