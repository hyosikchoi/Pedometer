package com.hyosik.android.pedometer

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.hyosik.android.accelerometer.utils.Accelerometer
import com.hyosik.android.detector.Detector
import com.hyosik.android.pedometer.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.btnStart.setOnClickListener {
            if (checkDetectorSensor()) {
               if(Build.VERSION.SDK_INT >= Q) {
                   if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                       != PackageManager.PERMISSION_GRANTED) {
                       // Permission is not granted
                       ActivityCompat.requestPermissions(this,
                           arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                           100)
                   } else {
                       Detector.registerSensorListener(this)
                   }
               } else {
                 Detector.registerSensorListener(this)
               }
            } else {
                Accelerometer.registerSensorListener(this)
            }
        }

        binding.btnStop.setOnClickListener {
            if (checkDetectorSensor()) {
                Detector.stopStep()
                Detector.unRegisterSensorListener()
            } else {
                Accelerometer.stopStep()
                Accelerometer.unRegisterSensorListener()
            }
        }

        lifecycleScope.launch {
            Accelerometer.numStepFlow.collect {
                binding.tvSteps.text = it.toString()
            }
        }

        lifecycleScope.launch {
            Detector.numStepFlow.collect {
                binding.tvSteps.text = it.toString()
            }
        }
    }

    private fun checkDetectorSensor(): Boolean {
        val mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null
    }

}