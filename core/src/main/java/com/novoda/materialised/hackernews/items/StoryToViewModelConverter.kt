package com.novoda.materialised.hackernews.items

fun convertStoryToViewModel(story: Story): StoryViewModel = StoryViewModel(story.by, story.kids, story.id, story.score, story.title, story.url)

