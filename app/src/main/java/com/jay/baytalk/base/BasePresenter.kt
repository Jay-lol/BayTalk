package com.jay.baytalk.base

interface BasePresenter<T> {
    fun takeView(view : T){}
}