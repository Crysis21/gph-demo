package com.hold1.awesomefeed.story

import android.animation.IntEvaluator
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View


class CropLayoutManager : LinearLayoutManager {


    companion object {
        val TAG = "CropLayoutManager"
    }

    var portSize = -1
    private val mShrinkAmount = 0.25f

    constructor(context: Context) : super(context) {}

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}


    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val orientation = orientation

        if (portSize == -1)
            portSize = (0.6f * height).toInt()

        if (orientation == LinearLayoutManager.VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            val midpoint = height / 2f

            var currentMin = Float.MAX_VALUE
            var focusedIndex = 0

            val bounds = 200
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2f
                val scale = 1f - Math.min(bounds.toFloat(), Math.abs(midpoint - childMidpoint)) / bounds.toFloat()
                if (scale > 0) {
                    portSize = IntEvaluator().evaluate(scale, portSize, child.height).toFloat().toInt()
                }

                if (Math.abs(childMidpoint - midpoint) < currentMin) {
                    currentMin = Math.abs(childMidpoint - midpoint)
                    focusedIndex = i
                }
            }

            val s0 = 1f
            val s1 = 1f - mShrinkAmount


            val topTh = midpoint - portSize / 2
            val bottomTh = midpoint + portSize / 2


            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child is ShrinkingContainer) {
                    val childMidpoint = getMidpoint(child)
                    val distance = Math.min(portSize.toFloat(), midpoint - childMidpoint)
                    val scale = s0 + (s1 - s0) * distance / portSize
                    val focusDistance = Math.abs(i - focusedIndex)
                    if (getPosition(child) == 0 || getPosition(child) == state!!.itemCount - 1) {
                        child.bottomScale = 1.0f
                        child.topScale = 1.0f
                    } else
                        if (scale <= 1.0f) {
                            if (child.top < topTh) {
                                val t1 = 1f - mShrinkAmount / (focusDistance + 1)
                                val t2 = 1f - mShrinkAmount / (focusDistance)
                                val ts = s0 + (t1 - s0) * distance / portSize
                                val bs = s0 + (t2 - s0) * distance / portSize
                                Log.d(TAG, "child=$i fd=$focusDistance scale=$scale ts=$ts bs=$bs" )
                                child.topScale = scale
                                child.midpoint = child.height - (child.bottom - topTh)
                                child.bottomScale = 1.0f
                            }
                        } else {
                            child.bottomScale = 2.0f - scale
                            if (child.top < bottomTh) {
                                child.topScale = 1.0f
                                child.midpoint = bottomTh - child.top
                            }

                        }
                    child.invalidate()
                }
            }
            return scrolled
        } else {
            return 0
        }

    }

    fun getMidpoint(view: View): Float {
        return (getDecoratedBottom(view) + getDecoratedTop(view)) / 2f
    }
}