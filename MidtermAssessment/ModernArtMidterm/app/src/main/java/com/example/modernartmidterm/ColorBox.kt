package com.example.modernartmidterm

import android.graphics.Color
import android.view.View

open class ColorBox(private val view: View, private val startColor: Int, private val endColor: Int/*, private val boxType: BoxType*/) {

    fun makeColorBox(){
        view.setBackgroundColor(startColor)
    }

    fun getBaseColor() : Int {
        return startColor
    }

    fun getEndColor() : Int {
        return endColor
    }

    fun setNewCurrColor(newColor : Int){
        view.setBackgroundColor(newColor)
    }
}