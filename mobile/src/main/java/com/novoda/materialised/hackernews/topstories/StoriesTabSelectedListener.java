package com.novoda.materialised.hackernews.topstories;

import android.support.design.widget.TabLayout;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.firebase.FirebaseStoryIdDatabase;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

import java.util.HashMap;
import java.util.Map;

class StoriesTabSelectedListener implements TabLayout.OnTabSelectedListener {

    private final static Map<String, String> tabTextStoryTypeMap = new HashMap<>();

    private final FirebaseDatabase firebaseDatabase;
    private final ItemsDatabase itemsDatabase;
    private final AsyncListView<StoryViewModel> asyncListView;
    private final MultipleTabView multipleTabView;
    private final Navigator navigator;

    StoriesTabSelectedListener(
            FirebaseDatabase firebaseDatabase,
            ItemsDatabase itemsDatabase,
            AsyncListView<StoryViewModel> asyncListView,
            Navigator navigator,
            MultipleTabView multipleTabView
    ) {
        this.firebaseDatabase = firebaseDatabase;
        this.itemsDatabase = itemsDatabase;
        this.asyncListView = asyncListView;
        this.navigator = navigator;
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
        FirebaseStoryIdDatabase storyIdDatabase = new FirebaseStoryIdDatabase(firebaseDatabase, storyType);
        StoriesPresenter presenter = new StoriesPresenter(
                storyIdDatabase, itemsDatabase, asyncListView, navigator
        );
        multipleTabView.usePresenter(presenter);
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
