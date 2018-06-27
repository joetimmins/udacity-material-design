package com.novoda.materialised.hackernews.asynclistview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.novoda.materialised.R;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class AsyncListViewPresenter<T extends ViewData<Integer>,
        V extends View & ModelledView<T>>
        implements AsyncListView<T> {

    private final View loadingView;
    private final RecyclerView topStoriesView;
    private final ModelledViewInflater<V> viewInflater;

    private SingleTypeAdapter<T, V> adapter;

    public AsyncListViewPresenter(View loadingView, RecyclerView topStoriesView, Class<V> viewClass) {
        this(loadingView, topStoriesView, new ModelledViewInflater<>(viewClass));
    }

    private AsyncListViewPresenter(View loadingView, RecyclerView topStoriesView, ModelledViewInflater<V> viewInflater) {
        this.loadingView = loadingView;
        this.topStoriesView = topStoriesView;
        this.viewInflater = viewInflater;
        init();
    }

    @Override
    public void updateWith(@NonNull List<ViewModel<T>> initialViewModelList) {
//        init();
//        topStoriesView.swapAdapter(adapter, false);
    }

    private void init() {
        TextView textView = (TextView) loadingView;
        textView.setText(textView.getContext().getResources().getString(R.string.loading_stories));
        loadingView.setVisibility(View.VISIBLE);
        adapter = new SingleTypeAdapter<>(viewInflater);
        topStoriesView.setAdapter(adapter);
    }

    @Override
    public void updateWith(@NonNull ViewModel<T> viewModel) {
        loadingView.setVisibility(View.GONE);
        adapter.updateWith(viewModel);
    }

    @Override
    public void showError(@NotNull Throwable throwable) {
        Log.e("HN", "something borkded", throwable);
        TextView textView = (TextView) loadingView;
        textView.setText(R.string.bork_bork);
        loadingView.setVisibility(View.VISIBLE);
    }

}
