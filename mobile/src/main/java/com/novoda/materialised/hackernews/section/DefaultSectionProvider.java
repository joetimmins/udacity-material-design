package com.novoda.materialised.hackernews.section;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class DefaultSectionProvider implements SectionProvider {
    @NotNull
    @Override
    public List<Section> provideSections() {
        return Arrays.asList(Section.values());
    }
}
