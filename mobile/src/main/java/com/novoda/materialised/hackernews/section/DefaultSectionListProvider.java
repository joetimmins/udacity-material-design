package com.novoda.materialised.hackernews.section;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class DefaultSectionListProvider implements SectionListProvider {
    @NotNull
    @Override
    public List<Section> provideSections() {
        return Arrays.asList(Section.values());
    }
}
