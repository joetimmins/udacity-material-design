package com.novoda.materialised.hackernews.topstories;

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
import com.novoda.materialised.R;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.AsyncListViewPresenter;
import com.novoda.materialised.hackernews.firebase.FirebaseItemsDatabase;
import com.novoda.materialised.hackernews.firebase.FirebaseSingleton;
import com.novoda.materialised.hackernews.firebase.FirebaseStoryIdDatabase;
import com.novoda.materialised.hackernews.topstories.database.ItemsDatabase;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

public final class HackerNewsStoriesActivity extends AppCompatActivity implements MultipleTabView {

    private StoriesPresenter storiesPresenter;

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

        AsyncListView<StoryViewModel> asyncListView = new AsyncListViewPresenter<>(
                mainActivityLayout.loadingView,
                mainActivityLayout.storiesView,
                StoryCardView.class
        );

        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(this);
        ItemsDatabase itemsDatabase = new FirebaseItemsDatabase(firebaseDatabase);

        mainActivityLayout.storyTypeTabLayout.addOnTabSelectedListener(
                new StoriesTabSelectedListener(firebaseDatabase, itemsDatabase, asyncListView, new IntentNavigator(this), this)
        );

        storiesPresenter = new StoriesPresenter(
                new FirebaseStoryIdDatabase(firebaseDatabase, "topstories"),
                itemsDatabase,
                asyncListView,
                new IntentNavigator(this)
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

    @Override
    public void usePresenter(StoriesPresenter presenter) {
        storiesPresenter = presenter;
        storiesPresenter.present();
    }
}
