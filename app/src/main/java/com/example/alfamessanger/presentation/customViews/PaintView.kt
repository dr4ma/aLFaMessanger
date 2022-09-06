package com.example.alfamessanger.presentation.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class PaintView constructor(
    context: Context,
    attrs: AttributeSet,
) : View(context, attrs) {

    private lateinit var btmBackground: Bitmap
    private lateinit var btmView: Bitmap
    private val mPaint = Paint()
    private val mPath = Path()
    private var colorBackground: Int = 0
    private var sizeBrush: Int = 0
    private var sizeEraser: Int = 0
    private var mX: Float = 0f
    private var mY: Float = 0f
    private lateinit var mCanvas: Canvas
    private val DEFFERENCE_SPACE = 4
    private val listAction = mutableListOf<Bitmap>()

    init {
        sizeEraser = 12
        sizeBrush = 12
        colorBackground = Color.TRANSPARENT

        mPaint.color = Color.BLACK
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeWidth = toPx(sizeBrush)
    }

    private fun toPx(sizeBrush: Int): Float {
        return sizeBrush * (resources.displayMetrics.density)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        btmBackground = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        btmView = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(btmView)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(colorBackground)
        canvas?.drawBitmap(btmBackground, 0f, 0f, null)
        canvas?.drawBitmap(btmView, 0f, 0f, null)
    }

    fun setColorBackground(color: Int) {
        colorBackground = color
        invalidate()
    }

    fun setSizeBrush(s: Int) {
        sizeBrush = s
        mPaint.strokeWidth = sizeBrush.toFloat()
    }

    fun setBrushColor(color: Int) {
        mPaint.color = color
    }

    fun setSizeEraser(s: Int) {
        sizeEraser = s
        mPaint.strokeWidth = sizeEraser.toFloat()
    }

    fun enableEraser() {
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun disableEraser() {
        mPaint.xfermode = null
        mPaint.shader = null
        mPaint.maskFilter = null
    }

    private fun addLastAction(bitmap: Bitmap) {
        listAction.add(bitmap)
    }

    fun getListActionsSize() : Int{
        return listAction.size
    }

    fun returnLastAction() {
        if (listAction.size > 0) {
            listAction.removeAt(listAction.size - 1)
            if (listAction.size > 0) {
                btmView = listAction[listAction.size - 1]
            } else {
                btmView = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            }
            mCanvas = Canvas(btmView)
            invalidate()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                addLastAction(getBitmap())
            }
        }
        return true
    }

    private fun touchUp(){
        mPath.reset()
    }

    private fun touchMove(x: Float?, y: Float?) {
        if (x != null && y != null) {
            val dx = abs(x - mX)
            val dy = abs(y - mY)

            if (dx >= DEFFERENCE_SPACE || dy >= DEFFERENCE_SPACE) {
                mPath.quadTo(x, y, (x + mX) / 2, (y + mY) / 2)

                mY = y
                mX = x

                mCanvas.drawPath(mPath, mPaint)
                invalidate()
            }
        }
    }

    private fun touchStart(x: Float?, y: Float?) {
        if (x != null && y != null) {
            mPath.moveTo(x, y)
            mX = x
            mY = y
        }
    }

    fun clearCanvas(){
        listAction.clear()
        btmView = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(btmView)
        invalidate()
    }

    fun getBitmap() : Bitmap{
        this.isDrawingCacheEnabled = true
        this.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(this.drawingCache)
        this.isDrawingCacheEnabled = false
        return bitmap
    }
}