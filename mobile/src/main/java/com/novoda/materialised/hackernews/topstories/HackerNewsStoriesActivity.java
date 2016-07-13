package com.novoda.materialised.hackernews.topstories;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.R;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.hackernews.asynclistview.AsyncListViewPresenter;
import com.novoda.materialised.hackernews.asynclistview.ModelledViewInflater;
import com.novoda.materialised.hackernews.firebase.FirebaseItemsDatabase;
import com.novoda.materialised.hackernews.firebase.FirebaseSingleton;
import com.novoda.materialised.hackernews.firebase.FirebaseStoryIdDatabase;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.topstories.database.StoryIdDatabase;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

import org.jetbrains.annotations.NotNull;

public final class HackerNewsStoriesActivity extends AppCompatActivity {

    private StoriesPresenter storiesPresenter;
    private StoryIdDatabase storyIdDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityBinding mainActivityLayout = DataBindingUtil.setContentView(this, R.layout.main_activity);

        setSupportActionBar(mainActivityLayout.toolbar);

        mainActivityLayout.plusFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Toast.makeText(HackerNewsStoriesActivity.this, "This button does nothing", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        final AsyncListViewPresenter<StoryViewModel, StoryCardView> storiesViewPresenter = new AsyncListViewPresenter<>(
                mainActivityLayout.loadingView,
                mainActivityLayout.storiesView,
                new ModelledViewInflater<>(StoryCardView.class)
        );

        mainActivityLayout.storiesView.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(this);
        storyIdDatabase = new FirebaseStoryIdDatabase(firebaseDatabase, "topstories");
        final ItemsDatabase itemsDatabase = new FirebaseItemsDatabase(firebaseDatabase);

        mainActivityLayout.storyTypeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabText = tab.getText() != null ? tab.getText().toString() : "broken";
                if (tabText.equalsIgnoreCase("Top Stories")) {
                    storyIdDatabase = new FirebaseStoryIdDatabase(firebaseDatabase, "topstories");
                    createPresenter(storyIdDatabase, itemsDatabase, storiesViewPresenter);
                    storiesPresenter.present();
                }
                if (tabText.equalsIgnoreCase("New")) {
                    storyIdDatabase = new FirebaseStoryIdDatabase(firebaseDatabase, "newstories");
                    createPresenter(storyIdDatabase, itemsDatabase, storiesViewPresenter);
                    storiesPresenter.present();
                }
                if (tabText.equalsIgnoreCase("Best")) {
                    storyIdDatabase = new FirebaseStoryIdDatabase(firebaseDatabase, "beststories");
                    createPresenter(storyIdDatabase, itemsDatabase, storiesViewPresenter);
                    storiesPresenter.present();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        createPresenter(storyIdDatabase, itemsDatabase, storiesViewPresenter);
    }

    private void createPresenter(
            StoryIdDatabase storyIdDatabase, ItemsDatabase itemsDatabase, AsyncListViewPresenter<StoryViewModel, StoryCardView> storiesViewPresenter
    ) {
        storiesPresenter = new StoriesPresenter(
                storyIdDatabase,
                itemsDatabase,
                storiesViewPresenter,
                new IntentNavigator()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        storiesPresenter.present();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class IntentNavigator implements Navigator {
        @Override
        public void navigateTo(@NotNull String uri) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        }
    }
}
