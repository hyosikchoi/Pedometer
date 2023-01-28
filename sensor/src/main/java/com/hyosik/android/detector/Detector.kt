package com.hyosik.android.detector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

object Detector : SensorEventListener {
    private var sensorManager: SensorManager? = null

    private val _numStepFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val numStepFlow
        get() = _numStepFlow

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            if(event.values[0] == 1.0f) {
                _numStepFlow.value += 1
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    fun registerSensorListener(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager?.let{
            it.registerListener(this, it.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR), SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun unRegisterSensorListener() {
        sensorManager?.unregisterListener(this)
        sensorManager = null
    }

    fun stopStep() {
        _numStepFlow.value = 0
    }
}