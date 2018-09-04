package com.hold1.awesomefeed.benchmark

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.hold1.awesomefeed.R
import kotlinx.android.synthetic.main.benchmark_activity.*

/**
 * Created by Cristian Holdunu on 29/08/2018.
 */
class BenchMarkActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.benchmark_activity)
        frescoBtn.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            intent.putExtra(FeedActivity.KEY_LIBRARY, FeedActivity.LIB_FRESCO)
            startActivity(intent)
        }
        glideBtn.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            intent.putExtra(FeedActivity.KEY_LIBRARY, FeedActivity.LIB_GLIDE)
            startActivity(intent)
        }
    }
}