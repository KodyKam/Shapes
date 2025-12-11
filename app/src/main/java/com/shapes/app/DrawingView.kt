package com.shapes.app

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var canvasBitmap: Bitmap? = null
    private var canvasCanvas: Canvas? = null
    private val paths = mutableListOf<Path>()
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private var currentPath = Path()
    private var lastX = 0f
    private var lastY = 0f

    init {
        setBackgroundColor(Color.WHITE)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvasCanvas = Canvas(canvasBitmap!!)
        canvasCanvas?.drawColor(Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (canvasBitmap != null) {
            canvas.drawBitmap(canvasBitmap!!, 0f, 0f, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath.moveTo(x, y)
                lastX = x
                lastY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2)
                canvasCanvas?.drawPath(currentPath, paint)
                invalidate()
                lastX = x
                lastY = y
                return true
            }
            MotionEvent.ACTION_UP -> {
                currentPath.lineTo(x, y)
                canvasCanvas?.drawPath(currentPath, paint)
                paths.add(currentPath)
                invalidate()
                return true
            }
        }
        return false
    }

    fun clearCanvas() {
        paths.clear()
        currentPath = Path()
        if (canvasBitmap != null && canvasCanvas != null) {
            canvasCanvas?.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
        }
        invalidate()
    }

    fun exportBitmap(): Bitmap? {
        return if (canvasBitmap != null) {
            Bitmap.createBitmap(canvasBitmap!!)
        } else {
            null
        }
    }

    fun cleanBitmap(outputSize: Int = 64): Bitmap? {
        return exportBitmap()?.let {
            Bitmap.createScaledBitmap(it, outputSize, outputSize, true)
        }
    }
}
