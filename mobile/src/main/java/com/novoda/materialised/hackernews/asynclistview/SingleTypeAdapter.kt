package com.novoda.materialised.hackernews.asynclistview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

internal class SingleTypeAdapter<T, V>(private val viewInflater: ModelledViewInflater<V>) :
    RecyclerView.Adapter<ModelledViewHolder<V>>() where T : UiData<Int>, V : View, V : ModelledView<T> {
    private val uiStates: MutableList<UiState<T>>

    init {
        this.uiStates = ArrayList()
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelledViewHolder<V> {
        val view = viewInflater.inflateUsing(parent)
        return ModelledViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelledViewHolder<V>, position: Int) {
        val view = holder.obtainHeldView()
        val viewModel = uiStates[position]
        view.updateWith(viewModel)
    }

    override fun getItemCount(): Int {
        return uiStates.size
    }

    override fun getItemId(position: Int): Long {
        return idFor(position)!!.toLong()
    }

    fun updateWith(uiState: UiState<T>) {
        for (i in uiStates.indices) {
            if (shouldUpdate(i, uiState)) {
                uiStates[i] = uiState
                notifyItemChanged(i)
                return
            }
        }
        uiStates.add(uiState)
        notifyItemChanged(uiStates.size - 1)
    }

    private fun shouldUpdate(position: Int, fullyPopulatedUiState: UiState<T>): Boolean {
        val id = idFor(position)
        val fullyPopulatedViewModelId = fullyPopulatedUiState.viewData.id
        return id == fullyPopulatedViewModelId
    }

    private fun idFor(position: Int): Int? {
        return uiStates[position].viewData.id
    }
}
