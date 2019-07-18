package com.novoda.materialised.hackernews.asynclistview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class ModelledViewHolder<V>(private val heldModelledView: V) : RecyclerView.ViewHolder(heldModelledView)
    where V : View, V : ModelledView<*> {

    fun obtainHeldView(): V {
        return heldModelledView
    }
}
