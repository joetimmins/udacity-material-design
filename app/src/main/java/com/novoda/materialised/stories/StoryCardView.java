package com.novoda.materialised.stories;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.materialised.R;
import com.novoda.materialised.hackernews.items.StoryViewModel;

import org.jetbrains.annotations.NotNull;

final class StoryCardView extends LinearLayout implements SingleTypeAdapter.UpdatableView<StoryViewModel> {

    private TextView titleView;

    public StoryCardView(Context context) {
        super(context);
        init(context);
    }

    public StoryCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StoryCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.story_card, this);
        titleView = (TextView) findViewById(R.id.title_view);
    }

    @Override
    public void updateWith(@NotNull StoryViewModel storyViewModel) {
        titleView.setText(storyViewModel.getTitle());
    }
}
