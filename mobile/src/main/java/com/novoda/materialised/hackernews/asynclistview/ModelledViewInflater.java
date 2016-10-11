package com.novoda.materialised.hackernews.asynclistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

final class ModelledViewInflater<V extends View & ModelledView<? extends ViewModel>> {
    private final Class<V> viewClass;

    public ModelledViewInflater(Class<V> viewClass) {
        this.viewClass = viewClass;
    }

    V inflateUsing(ViewGroup parent) {
        Constructor<V> constructor;
        V result;
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
