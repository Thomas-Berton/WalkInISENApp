package fr.isen.berton.projectapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import fr.isen.berton.projectapp.R
import kotlin.math.exp

class PlayerPenguin(context: Context, screenX: Int, screenY: Int){
    var playerGround: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pingouin_glissant)
    var playerAir: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pingouin_volant)

    var jumping = false

    val initTop = screenY/1.8f
    private val lastTop = screenY / 15f

    private val width = screenX/3.5f-screenX/7f
    private val height = screenY / 1.3f - initTop

    var initSpeed = 400f
    var speed = 0f

    //hitbox ok
    val position = RectF(screenX / 7f, initTop, screenX/3.5f, screenY / 1.3f)

    init {
        playerGround = Bitmap.createScaledBitmap(playerGround, width.toInt(), height.toInt(), false)
        playerAir = Bitmap.createScaledBitmap(playerAir, width.toInt(), height.toInt(), false)
    }

    fun update(fps: Long) {
        speed = 5000* exp(-1000/initSpeed)
        initSpeed++

        if (position.top > lastTop && jumping) {
            position.top -= speed / fps
            position.bottom -= speed / fps
        }
        if(position.top < initTop && !jumping){
            position.top += speed / fps
            position.bottom += speed / fps
        }
    }
}

