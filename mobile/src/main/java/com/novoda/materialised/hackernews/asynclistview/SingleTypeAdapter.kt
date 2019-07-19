package com.novoda.materialised.hackernews.asynclistview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class SingleTypeAdapter<T, V>(private val viewInflater: ModelledViewInflater<V>) :
    RecyclerView.Adapter<ModelledViewHolder<V>>() where T : UiData<Int>, V : View, V : ModelledView<T> {
    private val uiStates: MutableList<UiState<T>> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelledViewHolder<V> =
        ModelledViewHolder(viewInflater.inflateUsing(parent))

    override fun onBindViewHolder(holder: ModelledViewHolder<V>, position: Int) {
        val view = holder.obtainHeldView()
        val uiState = uiStates[position]
        view.updateWith(uiState)
    }

    override fun getItemCount(): Int = uiStates.size

    override fun getItemId(position: Int): Long = uiStates[position].data.id.toLong()

    fun updateWith(newUiState: UiState<T>) =
        when (val originalUiStatePosition = uiStates.indexOfFirst { it.data.id == newUiState.data.id }) {
            -1 -> {
                uiStates.add(newUiState)
                notifyItemChanged(uiStates.size - 1)
            }
            else -> {
                uiStates[originalUiStatePosition] = newUiState
                notifyItemChanged(originalUiStatePosition)
            }
        }
}
