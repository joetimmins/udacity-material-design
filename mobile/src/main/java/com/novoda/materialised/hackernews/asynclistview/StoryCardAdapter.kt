package com.novoda.materialised.hackernews.asynclistview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.novoda.materialised.R
import com.novoda.materialised.databinding.StoryCardBinding
import com.novoda.materialised.hackernews.stories.view.StoryUiData

internal class StoryCardAdapter : RecyclerView.Adapter<DataBindingHolder<StoryCardBinding>>(), ModelledView<StoryUiData> {
    private val uiStates: MutableList<UiState<StoryUiData>> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingHolder<StoryCardBinding> {
        val binding = DataBindingUtil.inflate<StoryCardBinding>(
            LayoutInflater.from(parent.context),
            R.layout.story_card,
            parent,
            false
        )
        return DataBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingHolder<StoryCardBinding>, position: Int) {
        holder.binding.uiState = uiStates[position]
    }

    override fun getItemCount(): Int = uiStates.size

    override fun getItemId(position: Int): Long = uiStates[position].data.id.toLong()

    override fun updateWith(uiState: UiState<StoryUiData>) =
        when (val originalUiStatePosition = uiStates.indexOfFirst { it.data.id == uiState.data.id }) {
            -1 -> {
                uiStates.add(uiState)
                notifyItemChanged(uiStates.size - 1)
            }
            else -> {
                uiStates[originalUiStatePosition] = uiState
                notifyItemChanged(originalUiStatePosition)
            }
        }
}

internal class DataBindingHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)
