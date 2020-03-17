package fr.isen.berton.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_quiz.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {
    private val TAG = "firebase"
    private val tipsList = arrayListOf<String>()
    private val questionList = arrayListOf<String>()
    private val r1List = arrayListOf<String>()
    private val r2List = arrayListOf<String>()
    private val rvList = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val database = FirebaseDatabase.getInstance()
        var index = (1..3).random()
        val myRef = database.getReference("tips")
        val myRefQ = database.getReference("quiz/questions")
        val myRefR1 = database.getReference("quiz/réponse1")
        val myRefR2 = database.getReference("quiz/réponse2")
        val myRefRV = database.getReference("quiz/réponseV")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val value = it.getValue(String::class.java)
                    Log.d(TAG, "Value is: $value")
                    tipsList.add(value ?: "")
                }
                displayRandomTips()            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        myRefQ.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                dataSnapshot.children.forEach {
                    val value = it.getValue(String::class.java)
                    questionList.add(value ?: "")
                }
                    displayRandomQuestion(index)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        r1.setOnClickListener {
            if (r1.isChecked === true ) {
                r2.isChecked = false
                r3.isChecked = false
            }
        }

        r2.setOnClickListener {
            if (r2.isChecked === true ) {
                r1.isChecked = false
                r3.isChecked = false
            }
        }

        r3.setOnClickListener {
            if (r3.isChecked === true ) {
                r1.isChecked = false
                r2.isChecked = false
            }
        }

        validateButton.setOnClickListener {
               val intent = Intent(this, QuizActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
               startActivity(intent)
           }
       }

    private fun displayRandomTips() {
        tipsView.isVisible = true
        val randomIndexList = Random.nextInt(tipsList.size)
        val element = tipsList[randomIndexList]
        tipsView.text = element
    }

    private fun displayRandomQuestion(index:Int){
        quizView.isVisible = true
            val enonce = questionList[index]
            quizView.text = enonce
        Log.d("coucou", "erreur lecture")
    }

    /*private fun displayRandomQuiz() {
        quizView.isVisible = true
        //val randomIndexList = Random.nextInt(quizList.size)
        val  enonce = quizList[0]
        val faux1 = quizList[1]
        val faux2 = quizList[2]
        val vrai = quizList[3]
        quizView.text = enonce
        r1.text = faux1
        r2.text = faux2
        r3.text = vrai
    }*/

}
