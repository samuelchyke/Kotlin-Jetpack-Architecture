package com.example.kotlin_jetpack_architecture.ui

interface DataStateChangeListener {

    fun onDataStateChange(dataState: DataState<*>?)

    fun expandAppBar()
}