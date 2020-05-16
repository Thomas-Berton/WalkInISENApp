package fr.isen.tavera.projetbusiness

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import kotlin.math.exp

class Ennemy(context: Context, private val screenX: Int, screenY: Int){

    var ours: Bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.ours)
    var avion:Bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.avion)
    var trou: Bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.trou)
    var orage: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.orage)

    private var random = 0

    val width = screenX/1.8f - screenX/3f
    private val height = screenY/1.2f - screenY/1.8f
    val airWidth = screenX/2.5f - screenX / 6f


    //Réduire taille hitbox pour simplifier le jeu si ça vous amuse
    //hitbox OK
    var positionSol = RectF(screenX + width, screenY/1.8f, screenX + 2*width, screenY/1.2f)
    val positionAir = RectF(screenX.toFloat(), screenY/15f,screenX  + airWidth, screenY/3f)

    var initSpeed = 200f
    private var speed = 0f

    init {
        ours = Bitmap.createScaledBitmap(ours, -width.toInt(), height.toInt(), false)
        avion = Bitmap.createScaledBitmap(avion, -width.toInt(), height.toInt(), false)
        trou = Bitmap.createScaledBitmap(trou, width.toInt(), height.toInt(), false)
        orage = Bitmap.createScaledBitmap(orage, width.toInt(), height.toInt(), false)

    }

    fun update(fps: Long): Int{
        speed = 5000*exp(-1000/initSpeed)
        initSpeed++

        when (random){
            in (0..1)->{
                positionSol.left -= speed / fps
                positionSol.right -= speed / fps
                if(positionSol.right < 0f && positionSol.left < 0){
                    positionSol.left = screenX.toFloat()
                    positionSol.right = screenX + width
                    random = (0..3).random()
                }
            }
            in (2..3)->{
                positionAir.left -= speed / fps
                positionAir.right -= speed / fps
                if(positionAir.right < 0f && positionAir.left < 0){
                    positionAir.left = screenX.toFloat()
                    positionAir.right = screenX + airWidth
                    random = (0..3).random()
                }
            }
        }
        return random
    }


}