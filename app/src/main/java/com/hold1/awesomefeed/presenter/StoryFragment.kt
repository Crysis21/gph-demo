package com.hold1.awesomefeed.presenter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hold1.awesomefeed.R
import java.util.*

/**
 * Created by Cristian Holdunu on 05/05/2018.
 */
class StoryFragment : Fragment() {

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
        view.setOnClickListener {
            if (it.scaleY == 1.0f) {
                it.scaleY = 0.9f
                it.scaleX = 0.9f
            } else {
                view.scaleX = 1.0f
                view.scaleY = 1.0f
            }
        }
    }
}