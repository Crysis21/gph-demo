package com.hold1.awesomefeed

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.hold1.awesomefeed.story.FeedAdapter
import com.hold1.awesomefeed.story.FocusLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storyList.layoutManager = FocusLayoutManager(this, storyList)
        SnapHelperOneByOne().attachToRecyclerView(storyList)
        storyList.adapter = FeedAdapter()
    }
}

class SnapHelperOneByOne : LinearSnapHelper() {
    var lastPosition = RecyclerView.NO_POSITION

    companion object {
        val TAG = "SnapHelperOneByOne"
    }

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {
        Log.d(TAG, "vY=$velocityY")
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return RecyclerView.NO_POSITION
        }

        val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION

        val currentPosition = layoutManager.getPosition(currentView)

        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION
        } else {
            if (lastPosition == RecyclerView.NO_POSITION) {
                lastPosition = currentPosition
            }
        }

        if (velocityY > 0) {
            lastPosition = lastPosition + 1
        } else {
            lastPosition = lastPosition - 1
        }

        return lastPosition
    }
}
