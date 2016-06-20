package com.novoda.materialised.hackernews.database

import com.novoda.materialised.hackernews.StoryViewModel

interface Items {
    fun readItem(id: Int, valueCallback: ValueCallback<StoryViewModel>)
}
