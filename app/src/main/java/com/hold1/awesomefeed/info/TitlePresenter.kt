package com.hold1.awesomefeed.info

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.hold1.awesomefeed.R
import kotlin.math.pow

/**
 * Created by Cristian Holdunu on 06/08/2018.
 */
class TitlePresenter @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class Direction {
        SLIDE_UP,
        SLIDE_DOWN
    }

    var text: String? = null
    var textSize = 40
    var textColor = Color.BLACK
    var textFont: String? = null
    var animator: ValueAnimator? = null
    private var slideDirection = Direction.SLIDE_UP

    private var currentView: View? = null
    private var slideView: View? = null
    private var slideLength = 0F


    init {

        val array = context.obtainStyledAttributes(attrs, R.styleable.TitlePresenter, 0, 0)
        textSize = array.getDimensionPixelSize(R.styleable.TitlePresenter_gphTextSize, textSize)
        textColor = array.getColor(R.styleable.TitlePresenter_gphTextColor, textColor)
        textFont = array.getString(R.styleable.TitlePresenter_gphFont)
        text = array.getString(R.styleable.TitlePresenter_gphText)
        array.recycle()

        addView(generateTextView(text ?: ""))
        animator = ValueAnimator.ofFloat(0f, 1f)
        animator?.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                if (slideDirection == Direction.SLIDE_UP)
                    (animation?.animatedValue as? Float)?.let {
                        slideView?.translationY = slideLength - it * slideLength
                        slideView?.alpha = it.pow(2)
                        currentView?.translationY = -it * slideLength
                        currentView?.alpha = 1f - it.pow(2)
                    }
                else
                    (animation?.animatedValue as? Float)?.let {
                        slideView?.translationY = -slideLength + it * slideLength
                        slideView?.alpha = it.pow(2)
                        currentView?.translationY = it * slideLength
                        currentView?.alpha = 1f - it.pow(2)
                    }
            }

        })
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                removeView(currentView)
                currentView = slideView
                slideView = null
            }

            override fun onAnimationCancel(animation: Animator?) {
                slideView?.translationY = 0f
                slideView?.alpha = 1f
                currentView?.alpha = 0f
            }

            override fun onAnimationStart(animation: Animator?) {
                currentView = getChildAt(0)
                addView(slideView)
                if (slideDirection == Direction.SLIDE_UP) {
                    slideView?.translationY = slideLength
                } else {
                    slideView?.translationY = -slideLength
                }
                slideView?.pivotY = slideView?.height!! / 2f
                currentView?.pivotY = currentView?.height!! / 2f
            }

        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        slideLength = h / 2F
    }

    private fun generateTextView(text: String): TextView {
        val textView = TextView(context)
        textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        textView.setTextColor(textColor)
        textView.text = text
        return textView
    }

    fun setText(text: String, direction: Direction) {
        animator?.cancel()
        this.text = text
        this.slideDirection = direction
        slideView = generateTextView(text)
        animator?.start()
    }
}