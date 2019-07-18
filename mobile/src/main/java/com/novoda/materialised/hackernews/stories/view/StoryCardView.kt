package com.novoda.materialised.hackernews.stories.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.novoda.materialised.R
import com.novoda.materialised.databinding.StoryCardBinding
import com.novoda.materialised.hackernews.asynclistview.ModelledView
import com.novoda.materialised.hackernews.asynclistview.UiState

class StoryCardView : LinearLayout, ModelledView<StoryUiData> {

    private var storyCard: StoryCardBinding? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    override fun updateWith(uiState: UiState<StoryUiData>) {
        val viewData = uiState.viewData
        if (viewData is StoryUiData.JustAnId) {
            storyCard!!.id = viewData
        }

        if (viewData is StoryUiData.FullyPopulated) {
            storyCard!!.viewData = viewData
            storyCard!!.viewModel = uiState
        }
    }

    private fun init(context: Context) {
        storyCard = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.story_card, this, true)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setLayoutParams(layoutParams)
    }
}
