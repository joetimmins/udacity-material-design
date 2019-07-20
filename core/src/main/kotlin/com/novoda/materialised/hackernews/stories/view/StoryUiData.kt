package com.novoda.materialised.hackernews.stories.view

import java.net.URI

data class StoryUiData(
    val by: String = "",
    val commentIds: List<Int> = emptyList(),
    val id: Int = 0,
    val score: Int = 0,
    val title: String = "",
    val url: String = ""
) {
    fun commentCount(): String = commentIds.count().toString()
    fun submittedFrom(): String {
        val domainName = URI.create(url).host ?: ""
        val prefix = "www."
        return when {
            domainName.startsWith(prefix) -> domainName.substring(prefix.length)
            else -> domainName
        }
    }
}
