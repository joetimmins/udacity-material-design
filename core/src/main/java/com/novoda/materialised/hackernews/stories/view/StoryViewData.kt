package com.novoda.materialised.hackernews.stories.view

import com.novoda.materialised.hackernews.asynclistview.ViewData
import java.net.URI
import java.util.*

data class StoryViewData(
        val by: String = "",
        val commentIds: List<Int> = Collections.emptyList(),
        override val id: Int = 0,
        val score: Int = 0,
        val title: String = "",
        val url: String = ""
) : ViewData<Int> {
    fun submittedFrom(): String {
        val domainName = URI.create(url).host ?: ""
        val prefix = "www."
        when {
            domainName.startsWith(prefix) -> return domainName.substring(prefix.length)
            else -> return domainName
        }
    }

    fun commentCount(): String {
        return commentIds.count().toString()
    }
}
