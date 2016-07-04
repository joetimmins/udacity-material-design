package com.novoda.materialised.hackernews.stories;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UpdatableViewInflater<T extends View & UpdatableView> {
    @LayoutRes
    private final int layoutRes;

    public UpdatableViewInflater(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public T inflateUpdatableView(ViewGroup parent) {
        return (T) LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }
}
