package com.jay.baytalk

interface OnItemClick {
    fun onClick(rid : String ,t : Any, i : Int){}
    fun onClick2(rid : String, t: Any, x : List<String>){}
}