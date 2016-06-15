package com.novoda.materialised.hackernews.database

import com.novoda.materialised.hackernews.StoryViewModel

interface HackerNewsItemDatabase {
    fun readItem(id: Int): StoryViewModel
}
