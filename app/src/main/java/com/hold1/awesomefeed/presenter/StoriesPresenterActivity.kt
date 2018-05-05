package com.hold1.awesomefeed.presenter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.hold1.awesomefeed.R
import kotlinx.android.synthetic.main.stories_presenter.*

/**
 * Created by Cristian Holdunu on 05/05/2018.
 */
class StoriesPresenterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stories_presenter)
        val adapter = PresenterAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
        viewPager.setPageTransformer(false, CustPagerTransformer(this))
    }

}

class PresenterAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return StoryFragment.newInstance()
    }

    override fun getCount(): Int {
        return 5
    }

}