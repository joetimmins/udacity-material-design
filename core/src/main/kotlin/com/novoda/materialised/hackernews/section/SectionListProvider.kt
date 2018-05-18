package com.novoda.materialised.hackernews.section

interface SectionListProvider {
    fun provideSections(): List<Section>
}
