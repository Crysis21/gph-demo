package com.hold1.awesomefeed

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import kotlin.math.max

/**
 * Created by Cristian Holdunu on 26/06/2018.
 */

class ExpandableButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        val TAG = ExpandableButton::class.java.simpleName
    }

    var startRadius = 10f
    var appliedRadius = startRadius
    var progress = 1.0f
        set(value) {
            field = value
            applyProgress(value)
        }
    var miniViewId: Int = View.NO_ID
    var expandViewId: Int = View.NO_ID
    var miniView: View? = null
    var expandView: View? = null

    var expandWidth = 0
    var miniWidth = 0

    init {
        setWillNotDraw(false)
        val array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableButton, 0, 0)
        miniViewId = array.getResourceId(R.styleable.ExpandableButton_miniView, View.NO_ID)
        expandViewId = array.getResourceId(R.styleable.ExpandableButton_expandView, View.NO_ID)
        array.recycle()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = CustomOutline()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        applyProgress(progress)
        if (miniWidth == 0)
            miniWidth = miniView?.width ?: 0
        if (expandWidth == 0)
            expandWidth = expandView?.width ?: 0
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        miniView = findViewById(miniViewId)
        expandView = findViewById(expandViewId)
    }

    private fun applyProgress(progress: Float) {
        miniView?.alpha = 1f - progress
        expandView?.alpha = progress
        appliedRadius = max(startRadius, (1f - progress) * height / 2)
        Log.d(TAG, "progress=$progress radius=$appliedRadius")
        val params = layoutParams
        layoutParams.width = ((1f - progress) * height + progress * expandWidth).toInt()
        layoutParams = params
        invalidateOutline()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class CustomOutline internal constructor() : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
//            if (drawAsCircle) {
//                outline.setOval(0, 0, width, height)
//            } else {
            outline.setRoundRect(0, 0, width, height, appliedRadius)
//            }
            view.clipToOutline = true
        }
    }
}