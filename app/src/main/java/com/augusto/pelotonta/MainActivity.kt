package com.augusto.pelotonta

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

lateinit var sensorManager : SensorManager
lateinit var accelerometer : Sensor

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myCanvasView = MyCanvasView(this)
        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(myCanvasView)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (start) {
            if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val tempPosX = posX - event.values[0] * 3
                val tempPosY = posY + event.values[1] * 3
                if (RADIUS <= tempPosX && tempPosX <= viewWidth - RADIUS) {
                    posX = tempPosX
                }
                if (RADIUS <= tempPosY && tempPosY <= viewHeight - RADIUS) {
                    posY = tempPosY
                }
            }
        }
    }

    override fun onAccuracyChanged(event: Sensor?, p1: Int) {
    }
}
