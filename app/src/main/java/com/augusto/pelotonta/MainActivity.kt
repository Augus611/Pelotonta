package com.augusto.pelotonta

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.widget.Toast
import androidx.annotation.RequiresApi

lateinit var sensorManager : SensorManager
lateinit var accelerometer : Sensor
lateinit var vibrator : Vibrator
var vibX = false
var vibY = false

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myCanvasView = MyCanvasView(this)
        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(myCanvasView)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        if (start) {
            if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                // Calculo la nueva posición
                val tempPosX = posX - event.values[0] * 3
                val tempPosY = posY + event.values[1] * 3
                // Si la nueva posición se encuentra dentro del rango del ancho de la pantalla, la aplico.
                if (RADIUS <= tempPosX && tempPosX <= viewWidth - RADIUS) {
                    posX = tempPosX
                    vibX = false
                // Si no, me fijo si está en el margen y además vibro una vez.
                } else {
                    when {
                        tempPosX <= RADIUS -> posX = RADIUS
                        tempPosX >= viewWidth - RADIUS -> posX = viewWidth.toFloat() - RADIUS
                    }
                    if (!vibX) {
                        vibrate()
                        vibX = true
                    }
                }
                // Ídem pero con el alto.
                if (RADIUS <= tempPosY && tempPosY <= viewHeight - RADIUS) {
                    posY = tempPosY
                    vibY = false
                } else {
                    when {
                        tempPosY <= RADIUS -> posY = RADIUS
                        tempPosY >= viewHeight - RADIUS -> posY = viewHeight.toFloat() - RADIUS
                    }
                    if (!vibY) {
                        vibrate()
                        vibY = true
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(event: Sensor?, p1: Int) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrate() {
        vibrator.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
