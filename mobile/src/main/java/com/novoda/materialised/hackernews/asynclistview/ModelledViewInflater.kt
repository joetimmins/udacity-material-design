package com.novoda.materialised.hackernews.asynclistview

import android.content.Context
import android.view.View
import android.view.ViewGroup

import java.lang.reflect.Constructor

internal class ModelledViewInflater<V>(private val viewClass: Class<V>) where V : View, V : ModelledView<*> {

    fun inflateUsing(parent: ViewGroup): V {
        val constructor: Constructor<V>
        try {
            constructor = viewClass.getDeclaredConstructor(Context::class.java)
            return constructor.newInstance(parent.context)
        } catch (e: Exception) { // this is horrendous, but multi-catching gives a compile error on API < 19
            e.printStackTrace()
            throw RuntimeException("Couldn't inflate ModelledView instance!")
        }
    }
}
