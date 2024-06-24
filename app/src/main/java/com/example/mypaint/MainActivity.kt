package com.example.mypaint

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val myCanvasView=MyCanvasView(this)
        myCanvasView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        myCanvasView.contentDescription=getString(R.string.canvasContentDescription)
        setContentView(myCanvasView)
    }
}