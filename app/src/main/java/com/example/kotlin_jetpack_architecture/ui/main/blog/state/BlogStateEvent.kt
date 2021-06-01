package com.example.kotlin_jetpack_architecture.ui.main.blog.state


sealed class BlogStateEvent {

    class BlogSearchEvent : BlogStateEvent()

    class None: BlogStateEvent()
}