package com.hold1.awesomefeed.presenter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

/**
 * Created by Cristian Holdunu on 05/05/2018.
 */
class StoryLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        val TAG = StoryLayout::class.java.simpleName
    }

    private val mDragHelper: ViewDragHelper
    private val moveDetector: GestureDetectorCompat
    private var mTouchSlop = 5
    private var originX: Int = 0
    private var originY: Int = 0
    private var bottomView: View? = null
    private var topView: View? = null
    private var dragTopDest = -500
    private var dragDistance = dragTopDest

    init {
        mDragHelper = ViewDragHelper.create(this, 10f, DragHelperCallback())
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP and ViewDragHelper.EDGE_BOTTOM)
        moveDetector = GestureDetectorCompat(context, MoveDetector())
        moveDetector.setIsLongpressEnabled(false)

        val configuration = ViewConfiguration.get(getContext())
        mTouchSlop = configuration.scaledTouchSlop
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        topView = getChildAt(childCount - 1)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (!changed) {
            return
        }

        super.onLayout(changed, left, top, right, bottom)

        originY = topView?.getY()?.toInt() ?: 0
        dragDistance = Math.abs(dragTopDest - originY)
    }

    internal inner class MoveDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, dx: Float,
                              dy: Float): Boolean {
            return Math.abs(dy) + Math.abs(dx) > mTouchSlop
        }
    }

    private fun processLinkageView() {
        if (topView!!.top >= dragTopDest) {
            val currentDistance = Math.abs(dragTopDest - topView!!.getTop())
            val distanceRatio = currentDistance.toFloat() / dragDistance

            Log.d(TAG, "distanceRatio=$distanceRatio")
            topView?.setAlpha(Math.pow(distanceRatio.toDouble(), 2.0).toFloat())
            setScaleX(0.9f + 0.1f * (1f - distanceRatio))
            setScaleY(0.9f + 0.1f * (1f - distanceRatio))
        }
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        try {
            mDragHelper.processTouchEvent(e)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val yScroll = moveDetector.onTouchEvent(ev)
        var shouldIntercept = false
        try {
            shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev)
        } catch (e: Exception) {
        }

        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            mDragHelper.processTouchEvent(ev)
        }

        return shouldIntercept && yScroll
    }


    private inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            if (changedView === topView) {
                processLinkageView()
            }
        }

        override fun tryCaptureView(child: View, pointerId: Int) = child === topView

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            if (top <= originY) {
                return top
            } else {
                return originY
            }
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int) = child.left

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var finalY = originY
            if (Math.abs(releasedChild.top - originY) > dragDistance / 3) {
                //fully expand
                finalY = dragTopDest
            }
            if (mDragHelper.smoothSlideViewTo(releasedChild, releasedChild.left, finalY)) {
                ViewCompat.postInvalidateOnAnimation(this@StoryLayout)
            }
        }
    }

}