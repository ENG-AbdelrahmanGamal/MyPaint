package com.example.mypaint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.text.style.BackgroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.concurrent.fixedRateTimer

private val STROKE_WIDTH = 14f   //this is about pen font size

class MyCanvasView(context: Context) : View(context) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitMap: Bitmap
    private val backgroudColor = ResourcesCompat.getColor(resources, R.color.background, null)
    private var drawColor = ResourcesCompat.getColor(resources, R.color.draw, null)
    private val drawFramColor = ResourcesCompat.getColor(resources, R.color.red, null)

    private val paint = Paint().apply {
        color = drawColor
        style = Paint.Style.STROKE
        //smooth out edges what is draw without effective shape
        isAntiAlias = true
        //Dithering effects how colors with higher_precisions are down
        isDither = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }
    private val paintForFram=Paint().apply {
        color=drawFramColor
        strokeWidth=10f
        style = Paint.Style.STROKE
        //smooth out edges what is draw without effective shape
        isAntiAlias = true
        //Dithering effects how colors with higher_precisions are down
        isDither = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND

    }
    private val path = Path()
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var frame : Rect

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //create instance of Bitmap with new width and height
        // third parameter is bitmap color configurations
        // recycle extraBitmap before creating the new one

        if (::extraBitMap.isInitialized) extraBitMap.recycle()
        extraBitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitMap)
        extraCanvas.drawColor(backgroudColor)
        val inset=40

       frame=Rect(inset,inset,width-inset,height-inset)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitMap, 0F, 0F, paint)
        canvas.drawRect(frame,paintForFram)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()


        }
        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val destanceX = Math.abs(motionTouchEventX - currentX)
        val destanceY = Math.abs(motionTouchEventY - currentY)
        if (destanceX >= touchTolerance||destanceY >= touchTolerance) {
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }
}