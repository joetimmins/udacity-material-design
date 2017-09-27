# Materialised
Hacker News meets Material Design.

In order to showcase some of the cool techniques detailed in Udacity's Material Design course, I've created a small app that lets you browse Hacker News.

Material effects include:
- Collapsing app bar with tabs, to switch between the top, new and best stories
- Seam-to-step transition when scrolling through the article list
- A floating action button

Coming soon:
- Fancy view animations for nice transitions!
- A comments page!

Some other attempted techniques on show here include:
- Hexagonal architecture, with the code split between `mobile` and `core` modules
- Feature-based unit testing - rather than using each class as a unit under test, we're writing tests for the `core` module's functionality between an input and an output. This means we're not gluing our tests directly to our implementation
- Exclusive use of Kotlin for the `core` module of the app
- Data pulled in from the Hacker News API using Firebase, detailed here https://github.com/HackerNews/API
- Replacing all Firebase callbacks with RxJava 2.1 Singles and Observables using a generic binding system
- An intention to achieve 100% test coverage of the `core` module
- A first draft of a pattern I like to call Android-as-a-factory
- An asynchronously-loading RecyclerView, which updates its views as their data is returned from Firebase
- A view model based ViewPager implementation which will hopefully use the new Android ViewModel architecture at some point
