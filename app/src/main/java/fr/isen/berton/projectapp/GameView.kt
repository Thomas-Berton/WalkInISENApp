package fr.isen.berton.projectapp

//TODO GameOver (restart ou quit)
//retirer la physique du pingouin pour plus de facilité
//rajouter pause (bouton + resume)
//cacher la barre de status et de nav
//changer la vitesse si ça vous amuse
//rajouter du son pour plus de FUN parce qu'on aime le fun nous
//iceberg simultanés à l'écran
//saut plus long si appui prolongé


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView

class GameView(context: Context, private val size: Point): SurfaceView(context), Runnable {


    private val gameThread = Thread(this)
    private var playing = false
    private var paused = true
    private var ennemyType = 0


    private var canvas: Canvas = Canvas()
    private var paint: Paint = Paint()

    private var playerPenguin = PlayerPenguin(context, size.x, size.y)
    private val ennemy = Ennemy(context, size.x, size.y)
    private val background = Background(context, size.x, size.y)

    private var score = 0

    private val prefs: SharedPreferences = context.getSharedPreferences("Jumping Penguin", Context.MODE_PRIVATE)
    private var highScore = prefs.getInt("highScore", 0)

    override fun run(){
        var fps: Long = 0

        while (playing){
            val startFrameTime = System.currentTimeMillis()

            if (!paused){
                update(fps)
            }

            draw()

            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if(timeThisFrame >= 1){
                fps = 1000 / timeThisFrame
            }
        }
        gameOver()

    }

    private fun update(fps: Long){
        playerPenguin.update(fps)
        ennemyType = ennemy.update(fps)
        background.update(fps)
        score ++

        if (playerPenguin.position.intersect(ennemy.positionAir) || playerPenguin.position.intersect(ennemy.positionSol)){
            gameOver()
        }
    }

    private fun draw(){
        if(holder.surface.isValid) {
            canvas = holder.lockCanvas()

            canvas.drawColor(Color.argb(255,140,184,255))
            paint.color = Color.argb(255, 255, 255, 255)
            canvas.drawRect(background.background, paint)

            canvas.drawBitmap(background.icebergList[background.randomIceberg], background.position.left, background.position.top, paint)

            //Draw all the game objects here
            if(playerPenguin.position.top < playerPenguin.initTop){
                canvas.drawBitmap(playerPenguin.playerAir, playerPenguin.position.left, playerPenguin.position.top, paint)
            } else {
                canvas.drawBitmap(
                    playerPenguin.playerGround,
                    playerPenguin.position.left,
                    playerPenguin.position.top,
                    paint
                )
            }
            when(ennemyType){
                0->{
                    canvas.drawBitmap(ennemy.ours ,ennemy.positionSol.left, ennemy.positionSol.top, paint)
                }
                1->{
                    canvas.drawBitmap(ennemy.trou ,ennemy.positionSol.left, ennemy.positionSol.top, paint)
                }
                2->{
                    canvas.drawBitmap(ennemy.avion ,ennemy.positionAir.left, ennemy.positionAir.top, paint)
                }
                3->{
                    canvas.drawBitmap(ennemy.orage,ennemy.positionAir.left, ennemy.positionAir.top, paint)
                }
            }

            paint.color = Color.argb(255,0,0,0)
            paint.textSize = 40f
            canvas.drawText("Score: $score", 20f, 40f, paint)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun pause(){
        playing = false
        try {
            gameThread.join()
        }   catch (e: InterruptedException){
            Log.e("Error", "Joining thread")
        }
    }

    fun resume(){
        playing = true
        gameThread.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action){
            MotionEvent.ACTION_DOWN->{
                if (paused){
                    paused = false
                }
                playerPenguin.jumping = true
            }
            MotionEvent.ACTION_UP->{
                playerPenguin.jumping = false
            }
        }

        return true
    }

    private fun gameOver(){
        playing = false
        gameThread.join()
        try {
            gameThread.join()
        }   catch (e: InterruptedException){
            Log.e("Error", "Joining thread")
        }
        val oldHighScore = prefs.getInt("highScore", 0)
        if (highScore > oldHighScore) {
            val editor = prefs.edit()
            editor.putInt("highScore", highScore).apply()
        }

        var builder = AlertDialog.Builder(context)
        builder.setMessage("Best score = $highScore").setCancelable(false)
            .setPositiveButton("Rejouer", DialogInterface.OnClickListener{dialog,_->
                playing = true
                score = 0
                playerPenguin.initSpeed = 600f
                ennemy.initSpeed = 450f
                background.initSpeed = 250f
                ennemy.positionAir.left = size.x.toFloat()
                ennemy.positionAir.right = size.x + ennemy.airWidth
                ennemy.positionSol.right = size.x + ennemy.width
                ennemy.positionSol.left = size.x + ennemy.width*2
                resume()
            })
            .setNegativeButton("Quitter", DialogInterface.OnClickListener{dialog,_->

            })

        val alert = builder.create()
        alert.setTitle("Your score: $score")
        alert.show()
    }
}