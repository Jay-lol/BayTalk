package com.jay.baytalk.base

interface BaseView<T> {
    fun showError(error: String)
    fun ShowToast(text : String)
    fun setPresenter(presenter : T)
}