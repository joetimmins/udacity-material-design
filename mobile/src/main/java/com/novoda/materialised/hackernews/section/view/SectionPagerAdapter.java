package com.novoda.materialised.hackernews.section.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novoda.materialised.R;
import com.novoda.materialised.hackernews.Presenter;
import com.novoda.materialised.hackernews.asynclistview.AsyncListView;
import com.novoda.materialised.hackernews.asynclistview.AsyncListViewPresenter;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;
import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.stories.view.FullStoryViewData;
import com.novoda.materialised.hackernews.stories.view.StoryCardView;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function1;

final class SectionPagerAdapter extends PagerAdapter {
    private final List<ViewModel<Section>> viewModels;
    private final Function1<AsyncListView<FullStoryViewData>, Presenter<Section>> sectionPresenterFactory;

    SectionPagerAdapter(List<ViewModel<Section>> viewModels, Function1<AsyncListView<FullStoryViewData>, Presenter<Section>> sectionPresenterFactory) {
        this.viewModels = viewModels;
        this.sectionPresenterFactory = sectionPresenterFactory;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        Context context = container.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View sectionView = layoutInflater.inflate(R.layout.section_view, container, false);
        container.addView(sectionView);

        TextView loadingView = sectionView.findViewById(R.id.loading_view);
        RecyclerView recyclerView = sectionView.findViewById(R.id.stories_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        AsyncListView<FullStoryViewData> asyncListView = new AsyncListViewPresenter<>(loadingView, recyclerView, StoryCardView.class);

        Presenter<Section> sectionPresenter = sectionPresenterFactory.invoke(asyncListView);
        Section viewData = viewModels.get(position).getViewData();
        sectionPresenter.present(viewData);

        return sectionView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Section section = viewModels.get(position).getViewData();
        return section.getUserFacingName();
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        View sectionView = (View) object;
        container.removeView(sectionView);
    }

    @Override
    public int getCount() {
        return viewModels.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }
}
