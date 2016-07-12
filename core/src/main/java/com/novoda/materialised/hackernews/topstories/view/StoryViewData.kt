package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.asynclistview.ViewData
import java.net.URI

data class StoryViewData(
        val by: String = "",
        val commentIds: List<Int> = java.util.Arrays.asList(0),
        override val id: Int = 0,
        val score: Int = 0,
        val title: String = "",
        val url: String = ""
) : ViewData {
    fun submittedFrom(): String {
        val domainName = URI.create(url).host
        val prefix = "www."
        when {
            domainName.startsWith(prefix) -> return domainName.substring(prefix.length)
            else -> return domainName
        }
    }
}
