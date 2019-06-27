package com.novoda.materialised.hackernews.stories.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

import com.novoda.materialised.R
import com.novoda.materialised.databinding.StoryCardBinding
import com.novoda.materialised.hackernews.asynclistview.ModelledView
import com.novoda.materialised.hackernews.asynclistview.ViewModel

class StoryCardView : LinearLayout, ModelledView<StoryViewData> {

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

    override fun updateWith(viewModel: ViewModel<StoryViewData>) {
        val viewData = viewModel.viewData
        if (viewData is StoryViewData.JustAnId) {
            storyCard!!.id = viewData
        }

        if (viewData is StoryViewData.FullyPopulated) {
            storyCard!!.viewData = viewData
            storyCard!!.viewModel = viewModel
        }
    }

    private fun init(context: Context) {
        storyCard = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.story_card, this, true)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setLayoutParams(layoutParams)
    }
}
