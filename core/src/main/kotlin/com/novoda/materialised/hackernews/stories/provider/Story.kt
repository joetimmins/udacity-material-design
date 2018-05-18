package com.novoda.materialised.hackernews.stories.provider

data class Story(
        val by: String = "",
        val descendants: Int = 0,
        val id: Int = 0,
        val kids: List<Int> = emptyList(),
        val score: Int = 0,
        val time: Int = 0,
        val title: String = "",
        val type: String = "",
        val url: String = ""
)

