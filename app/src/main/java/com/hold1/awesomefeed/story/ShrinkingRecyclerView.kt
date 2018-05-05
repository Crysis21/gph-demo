package com.hold1.awesomefeed.story

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * Created by Cristian Holdunu on 28/04/2018.
 */
class ShrinkingRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var topScale = 0.8f
    var bottomScale = 0.8f
    var topTh = -1
    var bottomTh = -1
    val contentPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (topTh==-1)
            topTh=0
        if (bottomTh==-1)
            bottomTh=height
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        contentPath.reset()

        val topDiff = (1f - topScale) * width
        val bottomDiff = (1f - bottomScale) * width
        contentPath.lineTo(topDiff / 2, 0f)
        contentPath.lineTo(canvas!!.width.toFloat() - topDiff / 2, 0f)
        if (topTh>-1) {
            contentPath.lineTo(canvas.width.toFloat(), topTh.toFloat())
        }
        if (bottomTh>-1){
            contentPath.lineTo(canvas.width.toFloat(), bottomTh.toFloat())
        }
        contentPath.lineTo(canvas.width.toFloat() - bottomDiff / 2, canvas.height.toFloat())
        contentPath.lineTo(bottomDiff / 2, canvas.height.toFloat())
        if (bottomTh>-1){
            contentPath.lineTo(0F, bottomTh.toFloat())
        }
        if (topTh>-1) {
            contentPath.lineTo(0F, topTh.toFloat())
        }
        contentPath.lineTo(topDiff / 2, 0f)
        contentPath.close()

        canvas.clipPath(contentPath)
    }

}