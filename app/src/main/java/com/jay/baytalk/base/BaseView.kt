package com.jay.baytalk.base

interface BaseView<T> {
    fun showError(error: String){}
    fun showToast(msg : String)
}