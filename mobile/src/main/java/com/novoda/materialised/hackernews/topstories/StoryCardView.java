package com.novoda.materialised.hackernews.topstories;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.materialised.R;
import com.novoda.materialised.hackernews.asynclistview.UpdatableView;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

public final class StoryCardView
        extends LinearLayout
        implements UpdatableView<StoryViewModel> {

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

    @Override
    public void updateWith(final StoryViewModel viewModel) {
        titleView.setText(viewModel.getViewData().getTitle());
        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getViewBehaviour().onClick(viewModel.getViewData());
            }
        });
    }

    private void init(Context context) {
        inflate(context, R.layout.story_card, this);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        titleView = (TextView) findViewById(R.id.title_view);
    }
}
