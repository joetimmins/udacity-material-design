package com.novoda.materialised.hackernews.section.view

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.novoda.materialised.R
import com.novoda.materialised.hackernews.Presenter
import com.novoda.materialised.hackernews.asynclistview.AsyncListView
import com.novoda.materialised.hackernews.asynclistview.AsyncListViewPresenter
import com.novoda.materialised.hackernews.asynclistview.ViewModel
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.view.StoryCardView
import com.novoda.materialised.hackernews.stories.view.StoryViewData

internal class SectionPagerAdapter(
    private val viewModels: List<ViewModel<Section>>,
    private val sectionPresenterFactory: Function1<AsyncListView<StoryViewData>, Presenter<Section>>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val layoutInflater = LayoutInflater.from(context)
        val sectionView = layoutInflater.inflate(R.layout.section_view, container, false)
        container.addView(sectionView)

        val loadingView = sectionView.findViewById<TextView>(R.id.loading_view)
        val recyclerView = sectionView.findViewById<RecyclerView>(R.id.stories_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val asyncListView = AsyncListViewPresenter(loadingView, recyclerView, StoryCardView::class.java)

        val sectionPresenter = sectionPresenterFactory.invoke(asyncListView)
        val viewData = viewModels[position].viewData
        sectionPresenter.present(viewData)

        return sectionView
    }

    override fun getPageTitle(position: Int): CharSequence {
        val section = viewModels[position].viewData
        return section.userFacingName
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val sectionView = `object` as View
        container.removeView(sectionView)
    }

    override fun getCount(): Int {
        return viewModels.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
