package com.novoda.materialised.hackernews.topstories;

import android.support.design.widget.TabLayout;

import java.util.HashMap;
import java.util.Map;

class StoriesTabSelectedListener implements TabLayout.OnTabSelectedListener {

    private final static Map<String, String> tabTextStoryTypeMap = new HashMap<>();

    private final MultipleTabView multipleTabView;

    StoriesTabSelectedListener(MultipleTabView multipleTabView) {
        this.multipleTabView = multipleTabView;
    }

    static {
        tabTextStoryTypeMap.put("Top Stories", "topstories");
        tabTextStoryTypeMap.put("New", "newstories");
        tabTextStoryTypeMap.put("Best", "beststories");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabText = tab.getText() != null ? tab.getText().toString() : "Top Stories";
        String storyType = tabTextStoryTypeMap.get(tabText);
        multipleTabView.onTabSelected(storyType);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // no-op
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // no-op
    }
}
