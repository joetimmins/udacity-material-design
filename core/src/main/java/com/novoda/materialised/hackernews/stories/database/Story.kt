package com.novoda.materialised.hackernews.stories.database

data class Story(
        val by: String = "",
        val descendants: Int = 0,
        val id: Int = 0,
        val kids: List<Int> = List(1, init = { 0 }),
        val score: Int = 0,
        val time: Int = 0,
        val title: String = "",
        val type: String = "",
        val url: String = ""
)
