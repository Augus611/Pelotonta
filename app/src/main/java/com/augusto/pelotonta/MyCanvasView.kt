package com.augusto.pelotonta

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

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
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawCircle(posX, posY, RADIUS, paint)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!start) {
            start = true
        } else {
            posX = event!!.x
            posY = event.y
            invalidate()
        }
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
        super.onSizeChanged(width, height, oldwidth, oldheight)
        posX = width/2f
        posY = height/2f
        viewWidth = width
        viewHeight = height
    }
}