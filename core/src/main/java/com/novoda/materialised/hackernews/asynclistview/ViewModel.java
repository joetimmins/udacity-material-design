package com.novoda.materialised.hackernews.asynclistview;

public interface ViewModel<T extends ViewData> {
    T viewData();

    ClickListener<T> viewBehaviour();
}
