package com.novoda.materialised.hackernews.stories;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.novoda.materialised.R;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.hackernews.Presenter;
import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.AsyncListViewPresenter;
import com.novoda.materialised.hackernews.section.DefaultSectionListProvider;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.section.AllSectionsPresenter;
import com.novoda.materialised.hackernews.section.view.AndroidTabView;
import com.novoda.materialised.hackernews.stories.database.DatabaseFactory;
import com.novoda.materialised.hackernews.stories.database.StoryIdProvider;
import com.novoda.materialised.hackernews.stories.database.StoryProvider;
import com.novoda.materialised.hackernews.stories.view.StoryCardView;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

public final class HackerNewsStoriesActivity extends AppCompatActivity {

    private AllSectionsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityBinding mainActivityLayout = DataBindingUtil.setContentView(this, R.layout.main_activity);

        setSupportActionBar(mainActivityLayout.toolbar);
        mainActivityLayout.storiesView.setLayoutManager(new LinearLayoutManager(this));
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

        AsyncListView<StoryViewData> asyncListView = new AsyncListViewPresenter<>(
                mainActivityLayout.loadingView,
                mainActivityLayout.storiesView,
                StoryCardView.class
        );

        StoryProvider storyProvider = DatabaseFactory.newStoryProvider(this);
        StoryIdProvider storyIdProvider = DatabaseFactory.newStoryIdProvider(this);

        Presenter<Section> storiesPresenter = new StoriesPresenter(
                storyIdProvider,
                storyProvider,
                asyncListView,
                new IntentNavigator(this)
        );

        presenter = new AllSectionsPresenter(
                new DefaultSectionListProvider(),
                new AndroidTabView(mainActivityLayout.sectionTabLayout),
                storiesPresenter
        );
        presenter.startPresenting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resumePresenting();
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
