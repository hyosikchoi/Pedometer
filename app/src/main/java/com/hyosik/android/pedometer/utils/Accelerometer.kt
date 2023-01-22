package com.hyosik.android.pedometer.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.hyosik.android.pedometer.listener.StepListener
import kotlinx.coroutines.flow.MutableStateFlow

object Accelerometer : SensorEventListener, StepListener {

    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null

    private val _numStepFlow : MutableStateFlow<Int> = MutableStateFlow(0)
    val numStepFLow
        get() = _numStepFlow

    init {
        simpleStepDetector = StepDetector().apply { registerListener(this@Accelerometer) }
    }

    override fun onSensorChanged(event : SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector!!.updateAccelerometer(event.timestamp, event.values[0], event.values[1], event.values[2])
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun step(timeNs: Long) {
        _numStepFlow.value += 1
    }

    fun registerSensorListener(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager?.let{
            it.registerListener(this, it.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun unRegisterSensorListener() {
        sensorManager?.unregisterListener(this)
        sensorManager = null
    }

    fun startStep() {
        _numStepFlow.value = 0
    }

}