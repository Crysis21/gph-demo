package com.hold1.awesomefeed.presenter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hold1.awesomefeed.R
import com.hold1.awesomefeed.SnapHelperOneByOne
import com.hold1.awesomefeed.story.FeedAdapter
import com.hold1.awesomefeed.story.FocusLayoutManager
import kotlinx.android.synthetic.main.story_fragment.*
import java.util.*

/**
 * Created by Cristian Holdunu on 05/05/2018.
 */
class StoryFragment : Fragment(), StoryLayout.AnimationListener {

    companion object {
        val TAG = StoryFragment::class.java.simpleName
        fun newInstance(): StoryFragment {
            return StoryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.story_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val baseColors = context!!.resources.getIntArray(R.array.story_base_colors)

        view.setBackgroundColor(baseColors[Random().nextInt(baseColors.size - 1)])

        storyList.layoutManager = FocusLayoutManager(context!!, storyList)
        SnapHelperOneByOne().attachToRecyclerView(storyList)
        storyList.adapter = FeedAdapter()

        storyLayout.addAnimationListener(this)

    }

    override fun animationUpdate(percentage: Float) {
        presentationOverlay.alpha = 1f - percentage
        val offset = context!!.resources.getDimensionPixelSize(R.dimen.close_btn_size)

        progress.translationX = (percentage * offset/2)
        progress.scaleX = 1f - (offset.toFloat() / progress.width) * percentage
    }

}