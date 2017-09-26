package com.novoda.materialised.hackernews.stories;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.novoda.materialised.R;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.hackernews.Presenter;
import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.section.AllSectionsPresenter;
import com.novoda.materialised.hackernews.section.DefaultSectionListProvider;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.section.view.AndroidTabsView;
import com.novoda.materialised.hackernews.stories.provider.ProviderFactory;
import com.novoda.materialised.hackernews.stories.provider.StoryIdProvider;
import com.novoda.materialised.hackernews.stories.provider.SingleStoryProvider;
import com.novoda.materialised.hackernews.stories.view.StoryViewData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.jvm.functions.Function1;

public final class HackerNewsStoriesActivity extends AppCompatActivity {

    private AllSectionsPresenter presenter;

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

        ViewPager sectionViewPager = mainActivityLayout.viewpager;

        SingleStoryProvider singleStoryProvider = ProviderFactory.newSingleStoryProvider(this);
        StoryIdProvider storyIdProvider = ProviderFactory.newStoryIdProvider(this);

        Function1<AsyncListView<StoryViewData>, Presenter<Section>> sectionPresenterFactory = StorySectionPresenterKt.partialPresenter(
                storyIdProvider,
                singleStoryProvider,
                new IntentNavigator(this),
                Schedulers.io(),
                AndroidSchedulers.mainThread()
        );

        presenter = new AllSectionsPresenter(
                new DefaultSectionListProvider(),
                new AndroidTabsView(sectionViewPager, mainActivityLayout.sectionTabLayout, sectionPresenterFactory)
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
