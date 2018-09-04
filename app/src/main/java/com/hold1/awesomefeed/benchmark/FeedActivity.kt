package com.hold1.awesomefeed.benchmark

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.hold1.awesomefeed.R
import kotlinx.android.synthetic.main.feed_activity.*

/**
 * Created by Cristian Holdunu on 29/08/2018.
 */
class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_activity)

//        recyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (intent.getIntExtra(KEY_LIBRARY, LIB_GLIDE) == LIB_GLIDE) {
            setupGlide()
        } else {
            setupFresco()
        }


    }

    fun setupGlide() {
        setTitle("Glide")
        recyclerView.adapter = GlideAdapter(ImageProvider.images)
    }

    fun setupFresco() {
        setTitle("Fresco")
        recyclerView.adapter = FrescoAdapter(ImageProvider.images)
    }

    companion object {
        val TAG = FeedActivity::class.java.simpleName
        val KEY_LIBRARY = "key_librar"
        val LIB_GLIDE = 1
        val LIB_FRESCO = 2
    }
}