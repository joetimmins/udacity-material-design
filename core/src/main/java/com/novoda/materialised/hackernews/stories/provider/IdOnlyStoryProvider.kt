package com.novoda.materialised.hackernews.stories.provider

import com.novoda.materialised.hackernews.section.Section
import io.reactivex.Observable

interface IdOnlyStoryProvider {
    fun readStoryIds(section: Section, callback: ValueCallback<List<Story>>): Observable<MutableList<Story>>?
}
