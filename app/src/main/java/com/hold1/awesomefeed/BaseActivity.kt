package com.hold1.awesomefeed

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.giphy.messenger.fragments.gifs.StoryPreviewAdapter
import com.hold1.awesomefeed.info.InfoPresenterView
import com.hold1.awesomefeed.info.TitlePresenter
import kotlinx.android.synthetic.main.base_activity.*
import kotlinx.android.synthetic.main.info_item.view.*
import java.util.*

/**
 * Created by Cristian Holdunu on 04/05/2018.
 */
class BaseActivity: AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        storiesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        storiesList.adapter = StoryPreviewAdapter(this)

        progressBar.segments = 10
        progressBar.setProgressColors(Color.GREEN, Color.BLUE)
        progressBar.start()
        nextBtn.setOnClickListener {

        }

        prevBtn.setOnClickListener {
            progressBar.setSegment(2)
        }
        
        changeTitle.setOnClickListener {
            titlePresenter.setText("Demo Mode", if (Random().nextBoolean()) TitlePresenter.Direction.SLIDE_DOWN else TitlePresenter.Direction.SLIDE_UP)
        }

        val infoAdapter = object: InfoPresenterView.PresenterAdapter() {
            override fun getCount(): Int {
                return 10
            }

            override fun generateView(layutInflater: LayoutInflater): View {
                return layoutInflater.inflate(R.layout.info_item, null)
            }

            override fun bindView(view: View, position: Int) {
                view.textItem.text = "just a text $position"
            }
        }

        infoPresenter.adapter = infoAdapter
    }
}