package com.novoda.materialised.hackernews.asynclistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

final class ModelledViewInflater<V extends View & ModelledView> {
    private final Class<V> viewClass;

    ModelledViewInflater(Class<V> viewClass) {
        this.viewClass = viewClass;
    }

    V inflateUsing(ViewGroup parent) {
        Constructor<V> constructor;
        try {
            constructor = viewClass.getDeclaredConstructor(Context.class);
            return constructor.newInstance(parent.getContext());
        } catch (Exception e) { // this is horrendous, but multi-catching gives a compile error on API < 19
            e.printStackTrace();
            throw new RuntimeException("Couldn't inflate ModelledView instance!");
        }
    }
}
