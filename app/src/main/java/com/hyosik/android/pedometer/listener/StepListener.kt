package com.hyosik.android.pedometer.listener

interface StepListener {
    fun step(timeNs: Long)
}