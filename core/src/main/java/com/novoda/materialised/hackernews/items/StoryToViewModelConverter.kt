package com.novoda.materialised.hackernews.items

fun convertStoryToViewModel(story: Story): StoryViewModel {
    return StoryViewModel(story.by, story.kids, story.id, story.score, story.title, story.url)
}

