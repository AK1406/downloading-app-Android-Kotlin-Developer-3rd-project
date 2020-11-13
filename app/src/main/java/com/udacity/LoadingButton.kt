package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.*
import kotlin.properties.Delegates


private var nonDownloadingColor = 0
private var downloadingColor =0
private var isClicked: Boolean =false

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0


    private var centralText ="DOWNLOAD"

    var mBgColor = 0
    var mFgColor:Int = 0
    var mPercentage = 0f

    var mPaintBar: Paint? = null
    var mPaintProgress: Paint? = null

    var mRectProgress: Rect? = null


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->


    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
       /// isClickable = true
        val typedArray: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingButton)

       // paint.color=Color.parseColor("#FF07C2AA")

            mBgColor= typedArray.getInt(R.styleable.LoadingButton_bgColor, R.color.colorPrimary)
            mFgColor = typedArray.getInt(R.styleable.LoadingButton_fgColor, R.color.colorPrimaryDark)
            mPercentage= typedArray.getFloat(R.styleable.LoadingButton_percentage, 50f)

             typedArray.recycle()
            mPaintBar = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaintProgress = Paint(Paint.ANTI_ALIAS_FLAG);


            mPaintBar!!.color = mBgColor
            mPaintProgress!!.color = mFgColor


    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


if(isClicked){
    animatePaintColor()
    }
        canvas?.drawRect(0.0F, 0.0F, widthSize.toFloat(), heightSize.toFloat(), mPaintBar!!)
      //  canvas?.drawRect(0.0F, 0.0F, 0.0F + getProgressWidth(), heightSize.toFloat(), mPaintBar!!)
        paint.color= Color.WHITE
        drawCenterText(canvas!!, 650.0F, 70.0F)


    }

    private fun getProgressWidth(): Int {
        return if (mPercentage in 0.0..100.0) {
            ((widthSize * mPercentage / 100).toInt())
        } else {
            0
        }
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


    private fun animatePaintColor() {
        val animator = ValueAnimator.ofInt(0, 255)
        animator.duration = 400
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            mPaintBar?.color = Color.argb(255,128, it.animatedValue as Int, 78)
        }
        animator.start()
        isClicked=false

        /*animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                paint.color = Color.YELLOW
            }
        })*/
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
/*
    override fun performClick(): Boolean {
        if (super.performClick()) return true
isClicked = true
        return true
    }*/

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