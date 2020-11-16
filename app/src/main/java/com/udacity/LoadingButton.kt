package com.udacity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.properties.Delegates


private var isClicked: Boolean =false

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var centralText ="DOWNLOAD"

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed)
    { p, old, new ->


    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
       /// isClickable = true
    }
    private val bgPaint: Int = Color.parseColor("#FF004349")


    override fun onDraw(canvas: Canvas?) {

        paint.color=Color.parseColor("#FF07C2AA")
        canvas?.drawRect(0.0F, 0.0F, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color= Color.WHITE
        drawCenterText(canvas!!, 650.0F, 70.0F)
        if(isClicked) {

            canvas.drawCircle(((width / 2) + 300.0).toFloat(), (height / 2).toFloat(), 35.0F, paint)
        }


        super.onDraw(canvas)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }



    private fun drawCenterText(canvas: Canvas, x: Float, y: Float) {
        val centralTextBounds = Rect()
        centralText = when(isClicked){
            true -> "We are loading"
            false -> "DOWNLOAD"
        }
        paint.getTextBounds(centralText, 0, centralText.length, centralTextBounds)
        val centralTextHeight = centralTextBounds.height()
        val centralTextWidth = centralTextBounds.width()
        val x1 = x - centralTextWidth / 2
        val y1 = y + centralTextHeight / 2
        canvas.drawText(centralText, x1, y1, paint)
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchY = event.y.toDouble()
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (touchY >= height / 2) {
                    isClicked = true
                }
                this.invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}