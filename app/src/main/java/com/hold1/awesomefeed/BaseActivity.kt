package com.hold1.awesomefeed

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.giphy.messenger.fragments.gifs.StoryPreviewAdapter
import kotlinx.android.synthetic.main.base_activity.*

/**
 * Created by Cristian Holdunu on 04/05/2018.
 */
class BaseActivity: AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        storiesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        storiesList.adapter = StoryPreviewAdapter(this)
    }
}