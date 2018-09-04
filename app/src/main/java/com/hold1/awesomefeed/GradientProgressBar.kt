package com.hold1.awesomefeed

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by Cristian Holdunu on 19/06/2018.
 */
class GradientProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var bgPaint: Paint
    var progressPaint: Paint
    var segments: Int = 0
        set(value) {
            field = value
            segmentStake = 1.0f / value
        }
    var timePerSegment = 5000.toLong()
    var animator: ValueAnimator? = null
    var currentSegment: Int = 0
    var currentProgress: Float = 0f
    var segmentStake = 0.0f
    private var startColor: Int = Color.RED
    private var endColor: Int = Color.BLUE

    init {
        bgPaint = Paint()
        bgPaint.color = Color.BLACK
        bgPaint.isAntiAlias = true

        progressPaint = Paint()
        progressPaint.color = Color.RED
        progressPaint.isAntiAlias = true

        animator = ValueAnimator.ofFloat(0f, 1f)
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
//                currentSegment++
//                animator?.setFloatValues(currentProgress, (currentSegment + 1) * segmentStake)
//                animator?.setDuration(timePerSegment)
//                animator?.start()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        
        animator?.addUpdateListener { 
            this.currentProgress = it.animatedValue as Float
            invalidate()
        }
        animator?.interpolator = LinearInterpolator()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        progressPaint.setShader(LinearGradient(0f,0f, width.toFloat(),height.toFloat(), intArrayOf(startColor, endColor), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        canvas?.drawRect(0f, 0f, currentProgress * width.toFloat(), height.toFloat(), progressPaint)
    }

    fun setProgressColors(startColor: Int, endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
        progressPaint.setShader(LinearGradient(0f,0f, width.toFloat(),height.toFloat(), intArrayOf(startColor, endColor), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP))
    }

    fun setSegment(progress: Int) {
        this.currentSegment = progress
        this.animator?.cancel()
        this.animator?.setFloatValues(currentProgress, (currentSegment + 1) * segmentStake)
        this.animator?.setDuration(300)
        this.animator?.start()
    }
    
    fun start() {
        animator?.setDuration(timePerSegment)
        animator?.setFloatValues(currentProgress, (currentSegment + 1) * segmentStake)
        animator?.start()
    }

    fun pause() {
        animator?.cancel()
    }
}