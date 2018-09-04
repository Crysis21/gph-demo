package com.hold1.awesomefeed.story

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Path
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet




/**
 * Created by Cristian Holdunu on 30/03/2018.
 */

class SkewContainer : ConstraintLayout {
    var camera = Camera()

    constructor(context: Context) : super(context) {
        commonInit(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        commonInit(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        commonInit(attrs)
    }

    private fun commonInit(attrs: AttributeSet?) {
        setWillNotDraw(false)

    }



    var topScale = 1.0f
    var bottomScale = 1.0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    companion object {
        private val TAG = SkewContainer::class.java.name
    }

    val contentPath = Path()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val matrix = canvas!!.matrix
        camera.save()
        camera.rotateX(-15f)
        camera.rotateY(0f)
        camera.rotateZ(0f)
        camera.getMatrix(matrix)

        val centerX = canvas.getWidth() / 2
        val centerY = canvas.getHeight() / 2
        matrix.preTranslate((-centerX).toFloat(), (-centerY).toFloat()) //This is the key to getting the correct viewing perspective
        matrix.postTranslate(centerX.toFloat(), centerY.toFloat())

        camera.restore()
        canvas.concat(matrix)

    }
}