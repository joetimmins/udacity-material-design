package com.novoda.materialised.hackernews.items

import com.novoda.materialised.hackernews.items.StoryViewModel
import com.novoda.materialised.hackernews.ValueCallback

interface ItemsDatabase {
    fun readItem(id: Int, valueCallback: ValueCallback<StoryViewModel>)
}
