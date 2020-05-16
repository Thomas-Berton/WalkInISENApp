package fr.isen.tavera.projetbusiness

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class GameRetry : AppCompatActivity() {

    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        gameView = GameView(this,size)

        setContentView(gameView)

        //Pour cacher la barre de nav
        /*window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_IMMERSIVE
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        actionBar?.hide()*/

    }


    override fun onResume() {
        super.onResume()

        gameView?.resume()
    }

    override fun onPause() {
        super.onPause()

        gameView?.pause()
    }

}
