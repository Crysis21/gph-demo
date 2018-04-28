package com.hold1.awesomefeed

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet


/**
 * Created by Cristian Holdunu on 30/03/2018.
 */

class ShrinkingContainer : ConstraintLayout {

    constructor(context: Context) : super(context) {
        commonInit(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        commonInit(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        commonInit(attrs)
    }

    private fun commonInit(attrs: AttributeSet?) {
        setWillNotDraw(false)

    }

    var topScale = 1.0f
    var bottomScale = 1.0f
    var midpoint = -1f


    companion object {
        private val TAG = ShrinkingContainer::class.java.name
    }

    val contentPath = Path()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        contentPath.reset()

        val topDiff = (1f - topScale) * width
        val bottomDiff = (1f - bottomScale) * width

        contentPath.lineTo(topDiff / 2, 0f)
        contentPath.lineTo(canvas!!.width.toFloat() - topDiff / 2, 0f)
        if (midpoint >= 0)
            contentPath.lineTo(canvas.width.toFloat(), midpoint)
        contentPath.lineTo(canvas.width.toFloat() - bottomDiff / 2, canvas.height.toFloat())
        contentPath.lineTo(bottomDiff / 2, canvas.height.toFloat())
        if (midpoint >= 0)
            contentPath.lineTo(0F, midpoint)
        contentPath.lineTo(topDiff / 2, 0f)
        contentPath.close()

        canvas.clipPath(contentPath)
    }

    fun clean(){
        topScale = 1.0f
        bottomScale = 1.0f
        midpoint = -1f
    }
}