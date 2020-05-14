package fr.isen.berton.projectapp

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    var currentDate = Date()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        button_signup.setOnClickListener(){
            doRegister()
        }





       /* date_input.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus) {
                date_input.clearFocus()
                val dialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        onDateChoose(year, month, dayOfMonth)
                    },
                    1990,
                    7,
                    25)
                dialog.show()
            }
        }*/

    }

    private fun doRegister () {
        val email = newEmail.text.toString()
        val password = newMdp.text.toString()
        val passwordConfirmed = newConfirmedMdp.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please be sure to fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }
        if(!email.contains("@isen.yncrea.fr")){
            Toast.makeText(this, "Utilisez un mail de type '@isen.yncrea.fr'", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.length > 5 && password == passwordConfirmed ) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        return@addOnCompleteListener
                    } else {
                        addUserToDataBase()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erreur... ${it.message} ", Toast.LENGTH_SHORT).show()
                    Log.d("SignUp", "FAILED to create account with succes... ${it.message} ")
                }
        }else {
            Toast.makeText(this, "Veuillez saisir le meme mot de passe (min 6 carateres)", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun addUserToDataBase () {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(newEmail.text.toString(),newSurName.text.toString(), newName.text.toString(),0)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("SignUp", "Compte cree avec succes, id : $uid ")
                startActivity(Intent(this, HomePageActivity::class.java))
            }
    }

    class User (val userEmail: String ,val userSurname: String ,val userName: String , val userScore: Int){
        constructor() : this("","","",0)
    }


/*    fun Test(){
        val database = FirebaseDatabase.getInstance()
        val data = database.getReference("Users")
        val newId = data.push().key.toString()
        val User = Users(newId,"louis le patron")
        data.child(newId).setValue(User)
    }*/

    fun updateUI(account: FirebaseUser?) {

        if (account != null) {

            Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }
/*
    fun onDateChoose(year: Int, month: Int, day: Int) {
        date_input.setText(String.format("%02d/%02d/%04d", day, month+1, year))
        Toast.makeText(this,
            "date : ${date_input.text.toString()}",
            Toast.LENGTH_LONG).show()
    }

    fun getAge(year: Int, month: Int, day: Int): Int {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val dateString = formatter.format(currentDate)
        val components = dateString.split("/")
        var age = components[2].toInt() - year
        if(components[1].toInt() < month){
            age--
        } else if (components[1].toInt() == month &&
            components[0].toInt() < day){
            age --
        }
        return age
    }*/

/*
    @RequiresApi(Build.VERSION_CODES.O)
*/
    /*fun createAccount(view: View) {

        val newuser = WriteData()
        if (input_confirm_password.text.toString() == input_password.text.toString()) {

            mAuth?.createUserWithEmailAndPassword(newEmail.text.toString(), input_password.text.toString())?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Inscription", "createUserWithEmail:success")
                    val user = mAuth?.currentUser

                    newuser.Register(user!!.uid, newEmail.text.toString(), newName.text.toString(), newSurmane.text.toString(), date_input.text.toString())
                    updateUI(user)


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Inscription", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

            }
        }

    }*/




}