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
import com.hold1.awesomefeed.R

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
    private var originY: Int = 0
    private var topView: View? = null
    private var dragDistance = 500
    private var dragTopDest = -dragDistance
    private var dragViewId = View.NO_ID

    init {
        mDragHelper = ViewDragHelper.create(this, 10f, DragHelperCallback())
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP and ViewDragHelper.EDGE_BOTTOM)
        moveDetector = GestureDetectorCompat(context, MoveDetector())
        moveDetector.setIsLongpressEnabled(false)

        val configuration = ViewConfiguration.get(getContext())
        mTouchSlop = configuration.scaledTouchSlop

        val array = context.obtainStyledAttributes(attrs, R.styleable.StoryLayout, 0, 0)
        dragViewId = array.getResourceId(R.styleable.StoryLayout_dragView, View.NO_ID)
        array.recycle()

    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        topView = findViewById(dragViewId)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (!changed) {
            return
        }

        super.onLayout(changed, left, top, right, bottom)
        originY = topView?.getY()?.toInt() ?: 0
        dragTopDest = originY - dragDistance
    }

    internal inner class MoveDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, dx: Float,
                              dy: Float): Boolean {
            return Math.abs(dy) + Math.abs(dx) > mTouchSlop
        }
    }

    private fun processLinkageView() {
        Log.d(TAG, "processLinkageView")
        val currentDistance = topView!!.getTop() - originY
        val distanceRatio = Math.min(1f, 1f - Math.abs(currentDistance.toFloat() / dragDistance))

        Log.d(TAG, "distance=$currentDistance distanceRatio=$distanceRatio")
        topView?.setAlpha(Math.pow(distanceRatio.toDouble(), 1.4).toFloat())
        setScaleX(0.9f + 0.1f * (1f - distanceRatio))
        setScaleY(0.9f + 0.1f * (1f - distanceRatio))
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
            if (top >= (originY - dragDistance) && top <= originY) {
                return top
            } else if (top > originY) {
                return originY
            } else if (top < originY - dragDistance) {
                return originY - dragDistance
            }
            return top
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