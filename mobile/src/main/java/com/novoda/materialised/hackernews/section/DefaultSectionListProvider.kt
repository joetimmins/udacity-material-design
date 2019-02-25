package com.novoda.materialised.hackernews.section

import java.util.Arrays

class DefaultSectionListProvider : SectionListProvider {
    override fun provideSections(): List<Section> {
        return Arrays.asList(*Section.values())
    }
}
