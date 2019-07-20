package com.novoda.materialised.hackernews.section.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.novoda.materialised.R
import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.AsyncListViewPresenter
import com.novoda.materialised.hackernews.asynclistview.UiModel
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.view.UiStory

internal class SectionPagerAdapter(
    private val uiModels: List<UiModel<Section>>,
    private val sectionPresenterFactory: (AsyncListView<UiStory>) -> Presenter<Section>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val layoutInflater = LayoutInflater.from(context)
        val sectionView = layoutInflater.inflate(R.layout.section_view, container, false)
        container.addView(sectionView)

        val loadingView = sectionView.findViewById<TextView>(R.id.loading_view)
        val recyclerView = sectionView.findViewById<RecyclerView>(R.id.stories_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val asyncListView = AsyncListViewPresenter(loadingView, recyclerView)

        val sectionPresenter = sectionPresenterFactory.invoke(asyncListView)
        val section = uiModels[position].data
        sectionPresenter.present(section)

        return sectionView
    }

    override fun getPageTitle(position: Int): CharSequence {
        val section = uiModels[position].data
        return section.userFacingName
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val sectionView = `object` as View
        container.removeView(sectionView)
    }

    override fun getCount(): Int {
        return uiModels.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
