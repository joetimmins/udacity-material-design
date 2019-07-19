package com.novoda.materialised.hackernews.stories.view

import com.novoda.materialised.hackernews.asynclistview.UiData
import java.net.URI

data class StoryUiData(
    val by: String = "",
    val commentIds: List<Int> = emptyList(),
    override val id: Int = 0,
    val score: Int = 0,
    val title: String = "",
    val url: String = ""
) : UiData<Int> {
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
