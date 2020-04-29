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
    private var index = 0
    private var answersPoints = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        //val answersPoints = arrayListOf<Int>()
       // var answersPoints = 0

        goodAnswer.isVisible = false
        badAnswer.isVisible = false
        pengouinGood.isVisible = false



        HomePagePicture.setOnClickListener {
        val intent = Intent(this, HomePageActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("points", answersPoints)


        Log.d("test2","Les points sont : "+answersPoints)
            setResult(RESULT_OK,intent)
        finish()
        }


        val database = FirebaseDatabase.getInstance()
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

       myRefR1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                dataSnapshot.children.forEach {
                    val value = it.getValue(String::class.java)
                    r1List.add(value ?: "")
                }
                displayRandomAnswer1(index)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        myRefR2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                dataSnapshot.children.forEach {
                    val value = it.getValue(String::class.java)
                    r2List.add(value ?: "")
                }
                displayRandomAnswer2(index)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        myRefRV.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                dataSnapshot.children.forEach {
                    val value = it.getValue(String::class.java)
                    rvList.add(value ?: "")
                }
                displayRandomAnswerV(index)
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
            //index++ appel a la fct des jours
            displayRandomQuestion(index)
            displayRandomAnswer1(index)
            displayRandomAnswer2(index)
            displayRandomAnswerV(index)
            if (r3.isChecked === true){
                answersPoints = answersPoints + 10
                goodAnswer.isVisible = true
                quizView.isVisible = false
                pengouinGood.isVisible = true
                r1.isVisible = false
                r2.isVisible = false
                r3.isVisible = false
            }
            if (r1.isChecked === true || r2.isChecked === true){
                badAnswer.isVisible = true
                quizView.isVisible = false
                r1.isVisible = false
                r2.isVisible = false
                r3.isVisible = false
            }
            validateButton.isVisible = false

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

    }

    private fun displayRandomAnswer1(index:Int){
        r1.isVisible = true
        val rep1 = r1List[index]
        r1.text = rep1
    }

    private fun displayRandomAnswer2(index:Int){
        r2.isVisible = true
        val rep2 = r2List[index]
        r2.text = rep2
    }

    private fun displayRandomAnswerV(index:Int){
        r3.isVisible = true
        val repV = rvList[index]
        r3.text = repV
    }
}