package com.novoda.materialised.hackernews.stories

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.novoda.materialised.R
import com.novoda.materialised.databinding.MainActivityBinding
import com.novoda.materialised.hackernews.section.AllSectionsPresenter
import com.novoda.materialised.hackernews.section.DefaultSectionListProvider
import com.novoda.materialised.hackernews.section.view.AndroidTabsView
import com.novoda.materialised.hackernews.stories.provider.ProviderFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HackerNewsStoriesActivity : AppCompatActivity() {

    private var presenter: AllSectionsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivityLayout = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)

        setSupportActionBar(mainActivityLayout.toolbar)
        mainActivityLayout.plusFab.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toast.makeText(this@HackerNewsStoriesActivity, "This button does nothing", Toast.LENGTH_SHORT).show()
            }
        }

        val sectionViewPager = mainActivityLayout.viewpager

        val storyIdProvider = ProviderFactory.newStoryIdProvider(this)
        val storyProvider = ProviderFactory.newStoryProvider(this)

        val sectionPresenterFactory = partialPresenter(
            storyIdProvider,
            storyProvider,
            IntentNavigator(this),
            Schedulers.io(),
            AndroidSchedulers.mainThread()
        )

        presenter = AllSectionsPresenter(
            DefaultSectionListProvider(),
            AndroidTabsView(sectionViewPager, mainActivityLayout.sectionTabLayout, sectionPresenterFactory)
        )
        presenter!!.startPresenting()
    }

    override fun onResume() {
        super.onResume()
        presenter!!.resumePresenting()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}
