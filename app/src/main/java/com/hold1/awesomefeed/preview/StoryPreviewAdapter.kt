package com.giphy.messenger.fragments.gifs

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.hold1.awesomefeed.R
import com.hold1.awesomefeed.presenter.StoriesPresenterActivity

/**
 * Created by Cristian Holdunu on 30/04/2018.
 */
class StoryPreviewAdapter(val context: Context) : RecyclerView.Adapter<StoryPreviewHolder>() {
    val zoomAmount = 0.1f
    val fadeAmount = 0.4f

    val baseColors: IntArray
    val topColors: IntArray
    var colorIndex = 0
    var animator = ValueAnimator.ofFloat()
    var recyclerView: RecyclerView? = null
    var focusedView: View? = null

    companion object {
        val TAG = StoryPreviewAdapter::class.java.simpleName
    }

    init {
        baseColors = context.resources.getIntArray(R.array.story_base_colors)
        topColors = context.resources.getIntArray(R.array.story_top_colors)
        assert(topColors.size == baseColors.size)
        animator.addUpdateListener(object : Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

            override fun onAnimationUpdate(animation: ValueAnimator?) {
                focusedView?.let {
                    it.scaleX = animation!!.animatedValue as Float
                    it.scaleY = animation.animatedValue as Float
                }
                for (i in 0 until (recyclerView?.layoutManager?.childCount ?: 0)) {
                    val child = recyclerView!!.layoutManager.getChildAt(i)
                    if (child != focusedView)
                        child.alpha = (1f - fadeAmount * animation!!.animatedFraction)
                }
            }

            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                focusedView = null
            }

            override fun onAnimationCancel(animation: Animator?) = Unit

            override fun onAnimationStart(animation: Animator?) = Unit
        })
        animator.duration = 150
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: StoryPreviewHolder, position: Int) {

        holder.bindStory(baseColors[colorIndex], topColors[colorIndex])
        colorIndex = (colorIndex + 1) % baseColors.size


        holder.itemView.setOnTouchListener({ view, event ->
            Log.d(TAG, event.toString())
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    focusedView = view
                    focusView()
                }
                MotionEvent.ACTION_UP -> {
                    focusedView = view
                    removeFocus()
                    val intent = Intent(context, StoriesPresenterActivity::class.java)
                    context.startActivity(intent)
                }
                MotionEvent.ACTION_CANCEL -> {
                    focusedView = view
                    removeFocus()
                }
            }
            return@setOnTouchListener true
        })
    }

    private fun focusView() {
        animator.cancel()
        focusedView?.let { view ->
            animator.setFloatValues(view.scaleY, 1.0f + zoomAmount)
            animator.start()
        }
    }

    private fun removeFocus() {
        animator.cancel()
        focusedView?.let { view ->
            animator.setFloatValues(1.0f, view.scaleY)
            animator.reverse()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryPreviewHolder {
        return StoryPreviewHolder(LayoutInflater.from(parent.context).inflate(R.layout.story_preview_item, parent, false))
    }
}