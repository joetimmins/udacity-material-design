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
import com.novoda.materialised.hackernews.asynclistview.UiState
import com.novoda.materialised.hackernews.section.Section
import com.novoda.materialised.hackernews.stories.view.StoryCardView
import com.novoda.materialised.hackernews.stories.view.StoryUiData

internal class SectionPagerAdapter(
    private val uiStates: List<UiState<Section>>,
    private val sectionPresenterFactory: Function1<AsyncListView<StoryUiData>, Presenter<Section>>
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
        val viewData = uiStates[position].data
        sectionPresenter.present(viewData)

        return sectionView
    }

    override fun getPageTitle(position: Int): CharSequence {
        val section = uiStates[position].data
        return section.userFacingName
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val sectionView = `object` as View
        container.removeView(sectionView)
    }

    override fun getCount(): Int {
        return uiStates.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
