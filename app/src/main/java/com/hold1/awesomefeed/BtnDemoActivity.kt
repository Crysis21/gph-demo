package com.hold1.awesomefeed

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.btn_activity.*

/**
 * Created by Cristian Holdunu on 26/06/2018.
 */
class BtnDemoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.btn_activity)

        progress.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                expandableButton.progress = progress / 100f
                expandableButton2.progress = progress / 100f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }


}