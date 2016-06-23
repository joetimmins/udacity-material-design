package com.novoda.materialised.hackernews.items

import java.util.*

data class StoryViewModel(
        val by: String = "",
        val commentIds: List<Int> = Arrays.asList(0),
        val id: Int = 0,
        val score: Int = 0,
        val title: String = "",
        val url: String = ""
) {}
