package com.novoda.materialised.hackernews.stories.provider

import io.reactivex.Observable

interface StoryProvider {
    fun readItems(ids: List<Int>, valueCallback: ValueCallback<Story>): Observable<Story>
}
