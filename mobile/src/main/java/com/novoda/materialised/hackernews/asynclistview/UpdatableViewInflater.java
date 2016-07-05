package com.novoda.materialised.hackernews.asynclistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UpdatableViewInflater<T extends View & UpdatableView> {
    private final Class<T> viewClass;

    public UpdatableViewInflater(Class<T> viewClass) {
        this.viewClass = viewClass;
    }

    public T inflateUpdatableView(ViewGroup parent) {
        Constructor<T> constructor;
        T result;
        try {
            constructor = viewClass.getDeclaredConstructor(Context.class);
            result = constructor.newInstance(parent.getContext());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't inflate UpdatableView instance!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't inflate UpdatableView instance!");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't inflate UpdatableView instance!");
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't inflate UpdatableView instance!");
        }
        return result;
    }
}
