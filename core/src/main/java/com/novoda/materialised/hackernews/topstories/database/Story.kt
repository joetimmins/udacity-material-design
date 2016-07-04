package com.novoda.materialised.hackernews.topstories.database

import java.util.*

data class Story(
        val by: String = "",
        val descendants: Int = 0,
        val id: Int = 0,
        val kids: List<Int> = Arrays.asList(0),
        val score: Int = 0,
        val time: Int = 0,
        val title: String = "",
        val type: String = "",
        val url: String = ""
) {}
