# udacity-material-design
An app based on the stuff on Udacity's Material Design course.

In order to showcase some of the cool techniques detailed in the course, I've created a small app that lets you browse Hacker News.

Some other cool techniques on show here include:
- An attempt at hexagonal architecture, with the code split between `app` and `core` modules
- Exclusive use of Kotlin for the `core` module of the app
- Data pulled in from the Hacker News API using Firebase, detailed here https://github.com/HackerNews/API
- An intention to achieve 100% test coverage of the `core` module
- A first draft of a pattern I like to call Android-as-a-factory

Stuff I haven't done yet but am planning to includes Rx integration and proper lazy-loading of story details.
