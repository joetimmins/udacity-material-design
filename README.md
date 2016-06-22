# Materialised
Hacker News meets Material Design.

In order to showcase some of the cool techniques detailed in Udacity's Material Design course, I've created a small app that lets you browse Hacker News.

Material effects that are planned include:
- Collapsing app bar with tabs, to switch between the top, new and best stories
- Seam-to-step transition when selecting a story to read
- A floating action button for submitting new articles
- Fancy view animations for nice transitions!

Some other attempted techniques on show here include:
- Hexagonal architecture, with the code split between `app` and `core` modules
- Exclusive use of Kotlin for the `core` module of the app
- Data pulled in from the Hacker News API using Firebase, detailed here https://github.com/HackerNews/API
- An intention to achieve 100% test coverage of the `core` module
- A first draft of a pattern I like to call Android-as-a-factory

Stuff I haven't done yet but am planning to includes Rx integration and proper lazy-loading of story details.
