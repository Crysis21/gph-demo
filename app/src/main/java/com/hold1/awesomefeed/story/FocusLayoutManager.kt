package com.hold1.awesomefeed.story

import android.animation.IntEvaluator
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View


class FocusLayoutManager(context: Context, var recyclerView: ShrinkingRecyclerView) : LinearLayoutManager(context) {


    companion object {
        val TAG = FocusLayoutManager::class.java.simpleName
    }

    var portSize = -1


    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val orientation = orientation

        if (portSize == -1)
            portSize = (0.6f * height).toInt()

        if (orientation == LinearLayoutManager.VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            val midpoint = height / 2f

            var currentMin = Float.MAX_VALUE

            val bounds = 0.3 * height
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2f
                val scale = 1f - Math.min(bounds.toFloat(), Math.abs(midpoint - childMidpoint)) / bounds.toFloat()
                if (scale > 0) {
                    portSize = IntEvaluator().evaluate(scale, portSize, child.height).toFloat().toInt()
                }

                if (Math.abs(childMidpoint - midpoint) < currentMin) {
                    currentMin = Math.abs(childMidpoint - midpoint)
                }
            }

            val topTh = midpoint - portSize / 2
            val bottomTh = midpoint + portSize / 2

            recyclerView.bottomTh = bottomTh.toInt()
            recyclerView.topTh = topTh.toInt()
//            recyclerView.invalidate()
            return scrolled
        } else {
            return 0
        }

    }

    fun getMidpoint(view: View): Float {
        return (getDecoratedBottom(view) + getDecoratedTop(view)) / 2f
    }
}