package com.hold1.awesomefeed.info

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import java.util.*

/**
 * Created by Cristian Holdunu on 25/05/2018.
 */
class InfoPresenterView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val NO_POSITION = -1
    var position = NO_POSITION

    private val animator: ValueAnimator
    private val layoutInflater: LayoutInflater
    private var topView: View? = null
    private var baseView: View? = null
    private var nextView: View? = null
    private var prevView: View? = null
    private var currentView: View? = null

    private var viewQueue = ArrayDeque<View>(2)

    private val SLIDE_UP = 0
    private val SLIDE_DOWN = 1

    private var animationType = -1


    var adapter: PresenterAdapter? = null
        set(value) {
            field = value
            initializeNewAdapter()
        }

    init {
        layoutInflater = LayoutInflater.from(this.context)

        animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animator ->
            topView?.translationY = animator.animatedValue as Float * height
            baseView?.translationY = height.toFloat() + animator.animatedValue as Float * height
//            topView?.alpha = 1f - animator.animatedValue as Float
//            baseView?.alpha = animator.animatedValue as Float
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                viewQueue.add(topView)
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
                topView = viewQueue.pop()
                baseView = viewQueue.last
                adapter?.bindView(baseView!!, position)

                baseView?.visibility = View.VISIBLE
                if (animationType == SLIDE_UP) {
                    baseView?.translationY = height.toFloat()
                } else if (animationType == SLIDE_DOWN) {
                    baseView?.translationY = -height.toFloat()
                }
            }
        })

        animator.setDuration(100)
    }

    private fun initializeNewAdapter() {
        removeAllViews()
        position = NO_POSITION

        val view1 = adapter!!.generateView(layoutInflater)
        val view2 = adapter!!.generateView(layoutInflater)
        addView(view1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(view2, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        viewQueue.add(view1)
        viewQueue.add(view2)

        topView = viewQueue.first
        baseView = viewQueue.last
        baseView?.visibility = View.INVISIBLE

        goToItem(0)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }

    fun goToItem(newPosition: Int) {
        if (position == NO_POSITION && newPosition == 0) {
            //Display first item without animation
            adapter?.bindView(topView!!, newPosition)
            position = newPosition
        } else {
            if (newPosition > position) {
                //Animate up
                animationType = SLIDE_UP
                animator.setFloatValues(0f, -1f)
                position = newPosition
                animator.start()
            } else if (newPosition < position) {
                //Animate down
                animationType = SLIDE_DOWN
                animator.setFloatValues(0f, 1f)
                position = newPosition
                animator.start()
            }
        }
    }

    abstract class PresenterAdapter {

        abstract fun getCount(): Int

        abstract fun generateView(layoutInflater: LayoutInflater): View

        abstract fun bindView(view: View, position: Int)
    }
}