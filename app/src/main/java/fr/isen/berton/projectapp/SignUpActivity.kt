package fr.isen.berton.projectapp

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    var currentDate = Date()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()


        Test()





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


    fun Test(){
        val database = FirebaseDatabase.getInstance()
        val data = database.getReference("Users")
        val newId = data.push().key.toString()
        val User = Users(newId,"louis le patron")
        data.child(newId).setValue(User)
    }

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