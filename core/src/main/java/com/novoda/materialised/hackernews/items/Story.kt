package com.novoda.materialised.hackernews.items

data class Story(
        val by: String = "",
        val descendants: Int = 0,
        val id: Int = 0,
        val kids: List<Int> = java.util.Arrays.asList(0),
        val score: Int = 0,
        val time: Int = 0,
        val title: String = "",
        val type: String = "",
        val url: String = ""
) {}
