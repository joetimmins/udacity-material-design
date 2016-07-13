package com.novoda.materialised.hackernews.topstories;

import android.support.design.widget.TabLayout;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.asynclistview.AsyncListViewPresenter;
import com.novoda.materialised.hackernews.firebase.FirebaseStoryIdDatabase;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

class StoriesTabSelectedListener implements TabLayout.OnTabSelectedListener {
    private final FirebaseDatabase firebaseDatabase;
    private final ItemsDatabase itemsDatabase;
    private final AsyncListViewPresenter<StoryViewModel, StoryCardView> storiesViewPresenter;
    private final MultipleTabView multipleTabView;
    private final Navigator navigator;

    public StoriesTabSelectedListener(
            FirebaseDatabase firebaseDatabase,
            ItemsDatabase itemsDatabase,
            AsyncListViewPresenter<StoryViewModel, StoryCardView> storiesViewPresenter,
            Navigator navigator,
            MultipleTabView multipleTabView
    ) {
        this.firebaseDatabase = firebaseDatabase;
        this.itemsDatabase = itemsDatabase;
        this.storiesViewPresenter = storiesViewPresenter;
        this.navigator = navigator;
        this.multipleTabView = multipleTabView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabText = tab.getText() != null ? tab.getText().toString() : "broken";
        if (tabText.equalsIgnoreCase("Top Stories")) {
            StoriesPresenter presenter = new StoriesPresenter(
                    new FirebaseStoryIdDatabase(firebaseDatabase, "topstories"), itemsDatabase, storiesViewPresenter, navigator
            );
            multipleTabView.usePresenter(presenter);
        }
        if (tabText.equalsIgnoreCase("New")) {
            StoriesPresenter presenter = new StoriesPresenter(
                    new FirebaseStoryIdDatabase(firebaseDatabase, "newstories"), itemsDatabase, storiesViewPresenter, navigator
            );
            multipleTabView.usePresenter(presenter);
        }
        if (tabText.equalsIgnoreCase("Best")) {
            StoriesPresenter presenter = new StoriesPresenter(
                    new FirebaseStoryIdDatabase(firebaseDatabase, "beststories"), itemsDatabase, storiesViewPresenter, navigator
            );
            multipleTabView.usePresenter(presenter);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
