package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.Presenter;
import com.novoda.materialised.hackernews.section.Section;

import java.util.ArrayList;
import java.util.List;

public final class SpyingSectionPresenter implements Presenter<Section> {
    public List<Section> presentedTypes = new ArrayList<>();

    @Override
    public void present(Section section) {
        presentedTypes.add(section);
    }
}
