package com.example.minumobat.util

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.GestureDetector
import java.lang.Exception

// kelas untuk deteksi gesture
// implementasi di menu saat slide keatas
// untuk buka menu home
class OnSwipeTouchListener : View.OnTouchListener {
    lateinit var gestureDetector: GestureDetector
    constructor(ctx: Context,onSwipeTop : () -> Unit) {
        gestureDetector = GestureDetector(ctx, GestureListener(onSwipeTop))
    }
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    private class GestureListener(var onSwipeTop : () -> Unit) : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent?): Boolean { return true }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2!!.y - e1!!.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            //onSwipeRight()
                        } else {
                            //onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        //onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }

    }
}