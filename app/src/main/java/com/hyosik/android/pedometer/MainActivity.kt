package com.hyosik.android.pedometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.hyosik.android.pedometer.databinding.ActivityMainBinding
import com.hyosik.android.pedometer.utils.Accelerometer
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.btnStart.setOnClickListener {
            Accelerometer.startStep()
            Accelerometer.registerSensorListener(this)
        }

        binding.btnStop.setOnClickListener {
            Accelerometer.unRegisterSensorListener()
        }

        lifecycleScope.launch {
            Accelerometer.numStepFLow.collect {
                 binding.tvSteps.text = it.toString()
            }
        }
    }
}