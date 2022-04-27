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
import android.view.WindowManager
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.math.pow

lateinit var sensorManager : SensorManager
lateinit var accelerometer : Sensor
lateinit var vibrator : Vibrator
lateinit var calendar0 : Calendar
var time0 = 0f
var timef = 0f
var v0x = 0f
var vfx = 0f
var v0y = 0f
var vfy = 0f
var vibX = false
var vibY = false

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myCanvasView = MyCanvasView(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
                // Calculo el instante de tiempo
                val calendarf = Calendar.getInstance()
                time0 = calendar0.timeInMillis.toFloat()
                timef = calendarf.timeInMillis.toFloat()
                var t = timef - time0
                if (t == 0f) {
                    // Porque al inicio el tiempo da 0 y si da 0 nunca se mueve.
                    t = 1f
                }
                // Calculo la nueva posición x = x0 + v0 * t + 1/2 * a * t^2
                val ax = event.values[0]
                vfx = v0x - ax * t
                val tempPosX = posX + v0x * t - 1/2 * ax * t.pow(2f)
                // Si la nueva posición se encuentra dentro del rango del ancho de la pantalla, la aplico.
                if (RADIUS <= tempPosX && tempPosX <= viewWidth - RADIUS) {
                    posX = tempPosX
                    if (RADIUS < tempPosX && tempPosX < viewWidth - RADIUS) {
                        vibX = false
                    }
                // Si no, me fijo si está en el margen y además vibro una vez.
                } else {
                    when {
                        tempPosX <= RADIUS -> posX = RADIUS
                        tempPosX >= viewWidth - RADIUS -> posX = viewWidth.toFloat() - RADIUS
                    }
                    vfx = 0f
                    if (!vibX) {
                        vibrate()
                        vibX = true
                    }
                }
                // Ídem pero con la posición y.
                val ay = event.values[1]
                vfy = v0y + ay * t
                val tempPosY = posY + v0y * t + 1/2 * ay * t.pow(2f)
                if (RADIUS <= tempPosY && tempPosY <= viewHeight - RADIUS) {
                    posY = tempPosY
                    if (RADIUS < tempPosY && tempPosY < viewHeight - RADIUS) {
                        vibY = false
                    }
                } else {
                    when {
                        tempPosY <= RADIUS -> posY = RADIUS
                        tempPosY >= viewHeight - RADIUS -> posY = viewHeight.toFloat() - RADIUS
                    }
                    vfy = 0f
                    if (!vibY) {
                        vibrate()
                        vibY = true
                    }
                }
                v0x = vfx
                v0y = vfy
                calendar0 = calendarf
            }
        }
    }

    override fun onAccuracyChanged(event: Sensor?, p1: Int) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrate() {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
