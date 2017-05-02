package com.novoda.materialised.hackernews.section

interface SectionProvider {
    fun provideSections(): List<Section>
}
