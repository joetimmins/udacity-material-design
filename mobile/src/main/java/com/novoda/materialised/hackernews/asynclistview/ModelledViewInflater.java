package com.novoda.materialised.hackernews.asynclistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

public final class ModelledViewInflater<T extends View & ModelledView> {
    private final Class<T> viewClass;

    public ModelledViewInflater(Class<T> viewClass) {
        this.viewClass = viewClass;
    }

    public T inflateUsing(ViewGroup parent) {
        Constructor<T> constructor;
        T result;
        try {
            constructor = viewClass.getDeclaredConstructor(Context.class);
            result = constructor.newInstance(parent.getContext());
        } catch (Exception e) { // this is horrendous, but multi-catching gives a compile error on API < 19
            e.printStackTrace();
            throw new RuntimeException("Couldn't inflate ModelledView instance!");
        }
        return result;
    }
}
