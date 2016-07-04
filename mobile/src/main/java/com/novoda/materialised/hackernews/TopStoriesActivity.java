package com.novoda.materialised.hackernews;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.R;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.hackernews.firebase.FirebaseItemsDatabase;
import com.novoda.materialised.hackernews.firebase.FirebaseSingleton;
import com.novoda.materialised.hackernews.firebase.FirebaseTopStoriesDatabase;
import com.novoda.materialised.hackernews.navigator.Navigator;
import com.novoda.materialised.hackernews.stories.AsyncListViewPresenter;
import com.novoda.materialised.hackernews.stories.StoryCardView;
import com.novoda.materialised.hackernews.stories.UpdatableViewInflater;
import com.novoda.materialised.hackernews.topstories.TopStoriesPresenter;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

import org.jetbrains.annotations.NotNull;

public final class TopStoriesActivity extends AppCompatActivity {

    private TopStoriesPresenter topStoriesPresenter;

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
                            Toast.makeText(TopStoriesActivity.this, "This button does nothing", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        mainActivityLayout.topStoriesView.setLayoutManager(new LinearLayoutManager(this));

        AsyncListViewPresenter<StoryViewModel, StoryCardView> storiesViewPresenter = new AsyncListViewPresenter<>(
                mainActivityLayout.loadingView,
                mainActivityLayout.topStoriesView,
                new UpdatableViewInflater<StoryCardView>(R.layout.inflatable_story_card)
        );

        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(this);
        topStoriesPresenter = new TopStoriesPresenter(
                new FirebaseTopStoriesDatabase(firebaseDatabase),
                new FirebaseItemsDatabase(firebaseDatabase),
                storiesViewPresenter,
                new IntentNavigator()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        topStoriesPresenter.present();
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
