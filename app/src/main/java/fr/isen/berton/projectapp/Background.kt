package fr.isen.tavera.projetbusiness

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import kotlin.math.exp
import kotlin.math.hypot

class Background(context: Context, screenX: Int, screenY: Int) {
    val background = RectF(0f, screenY / 1.5f, screenX.toFloat(), screenY.toFloat())

    private var height = screenY/10f
    private var width = screenX/10f
    private var initLeft = screenX.toFloat()
    var initSpeed = 50f
    private var speed = 0f

    var randomIceberg = 0
    var position = RectF(initLeft, screenY/3f, initLeft + width, screenY/3f)

    var icebergList = ArrayList<Bitmap>()

    init {
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg1))
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg2))
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg3))
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg4))
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg5))
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg6))
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg7))
        icebergList.add(BitmapFactory.decodeResource(context.resources, R.drawable.iceberg8))

        icebergList[0] = Bitmap.createScaledBitmap(icebergList[0], 2*width.toInt(), height.toInt(), false)
        icebergList[1] = Bitmap.createScaledBitmap(icebergList[1], width.toInt(), 3*height.toInt(), false)
        icebergList[2] = Bitmap.createScaledBitmap(icebergList[2], (1.5*width).toInt(), (1.5*height).toInt(), false)
        icebergList[3] = Bitmap.createScaledBitmap(icebergList[3], 5*width.toInt(), 2*height.toInt(), false)
        icebergList[4] = Bitmap.createScaledBitmap(icebergList[4], width.toInt(), height.toInt(), false)
        icebergList[5] = Bitmap.createScaledBitmap(icebergList[5], width.toInt(), 2*height.toInt(), false)
        icebergList[6] = Bitmap.createScaledBitmap(icebergList[6], width.toInt(), height.toInt(), false)
        icebergList[7] = Bitmap.createScaledBitmap(icebergList[7], 3*width.toInt(), height.toInt(), false)

    }

    fun update(fps:Long){
        speed = 5000* exp(-1000/initSpeed)
        initSpeed++

        position.right -= speed/fps
        position.left -= speed/fps
        if (position.right < 0 && position.left < 0){
            position.right = initLeft + width
            position.left = initLeft
            randomIceberg = (0..7).random()
        }
    }
}