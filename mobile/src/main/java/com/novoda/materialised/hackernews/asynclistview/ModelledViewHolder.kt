package com.novoda.materialised.hackernews.asynclistview

import android.support.v7.widget.RecyclerView
import android.view.View

internal class ModelledViewHolder<V>(private val heldModelledView: V) : RecyclerView.ViewHolder(heldModelledView)
    where V : View, V : ModelledView<*> {

    fun obtainHeldView(): V {
        return heldModelledView
    }
}
