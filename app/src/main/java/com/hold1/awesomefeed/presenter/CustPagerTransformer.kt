package com.hold1.awesomefeed.presenter

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View

/**
 * 实现ViewPager左右滑动时的时差
 * Created by xmuSistone on 2016/9/18.
 */
class CustPagerTransformer(context: Context) : ViewPager.PageTransformer {

    companion object {
        val TAG = CustPagerTransformer::class.java.simpleName
    }

    private val maxTranslateOffsetX: Int
    private var viewPager: ViewPager? = null

    init {
        this.maxTranslateOffsetX = dp2px(context, 580f)
    }

    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }
        
        val leftInScreen = view.left - viewPager!!.scrollX
        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager!!.measuredWidth / 2
        val offsetRate = offsetX.toFloat() * 0.18f / viewPager!!.measuredWidth
        Log.d(TAG, "position=$position  ofRate=$offsetRate")
        val scaleFactor = 1 - Math.abs(offsetRate) - 0.1f

        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.translationX = -maxTranslateOffsetX * offsetRate
            ViewCompat.setElevation(view, scaleFactor * 20)
        }
    }

    /**
     * dp和像素转换
     */
    private fun dp2px(context: Context, dipValue: Float): Int {
        val m = context.resources.displayMetrics.density
        return (dipValue * m + 0.5f).toInt()
    }

}
