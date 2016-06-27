package com.novoda.materialised;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.firebase.FirebaseItemsDatabase;
import com.novoda.materialised.firebase.FirebaseSingleton;
import com.novoda.materialised.firebase.FirebaseTopStoriesDatabase;
import com.novoda.materialised.hackernews.items.StoryViewModel;
import com.novoda.materialised.hackernews.topstories.StoriesView;
import com.novoda.materialised.hackernews.topstories.TopStoriesPresenter;
import com.novoda.materialised.stories.SingleViewModelTypeAdapter;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class TopStoriesActivity extends AppCompatActivity {

    private MainActivityBinding mainActivityLayout;
    private TopStoriesPresenter topStoriesPresenter;
    private StoriesView storiesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityLayout = DataBindingUtil.setContentView(this, R.layout.main_activity);

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

        storiesView = new StoriesView() {

            private SingleViewModelTypeAdapter<StoryViewModel> adapter;

            @Override
            public void updateWith(@NotNull StoryViewModel storyViewModel) {
                adapter.updateWith(storyViewModel);
            }

            @Override
            public void updateWith(@NotNull final List<StoryViewModel> storyViewModels) {
                mainActivityLayout.loadingView.setVisibility(View.GONE);
                adapter = new SingleViewModelTypeAdapter<>(storyViewModels, R.layout.inflatable_story_card);
                mainActivityLayout.topStoriesView.swapAdapter(adapter, false);
            }
        };

        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(this);
        topStoriesPresenter = new TopStoriesPresenter(new FirebaseTopStoriesDatabase(firebaseDatabase), new FirebaseItemsDatabase(firebaseDatabase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        topStoriesPresenter.presentMultipleStoriesWith(storiesView);
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

}
