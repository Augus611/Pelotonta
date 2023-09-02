package com.augusto.pelotonta

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.util.*

const val RADIUS = 128f
var posX = 0f
var posY = 0f
var viewWidth = 0
var viewHeight = 0
var start = false

class MyCanvasView (context: Context) : View(context){
    private val drawColor = ResourcesCompat.getColor(resources, R.color.purple_500, null)
    private val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
    }
//    This is for testing:
//    private val textColor = ResourcesCompat.getColor(resources, R.color.white, null)
//    private val textPaint = Paint().apply {
//        color = textColor
//        textSize = 50f
//    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(ResourcesCompat.getColor(resources, R.color.black, null))
        canvas?.drawCircle(posX, posY, RADIUS, paint)
//        val testingText = "vx=$vfx,vy=$vfy"
//        canvas?.drawText(testingText, 50f, 100f, textPaint)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                posX = event.x
                posY = event.y
                start = false
                v0x = 0f
                vfx = 0f
                v0y = 0f
                vfy = 0f
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                posX = event.x
                posY = event.y
                start = false
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                start = true
                calendar0 = Calendar.getInstance()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
        super.onSizeChanged(width, height, oldwidth, oldheight)
        if (oldwidth == 0 && oldheight == 0) {
            posX = width/2f
            posY = height/2f
        }
        viewWidth = width
        viewHeight = height
    }
}