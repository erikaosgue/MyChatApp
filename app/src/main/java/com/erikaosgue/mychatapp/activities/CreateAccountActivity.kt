package com.erikaosgue.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.erikaosgue.mychatapp.databinding.ActivityCreateAccountBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null


    lateinit var  actCreateAccountBinding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actCreateAccountBinding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(actCreateAccountBinding.root)


        mAuth = FirebaseAuth.getInstance()




        actCreateAccountBinding.accountCreateButton.setOnClickListener {

            val email = actCreateAccountBinding.accountEmail.text.toString().trim()
            val password = actCreateAccountBinding.accountPassword.text.toString().trim()
            val name = actCreateAccountBinding.accountDisplayName.text.toString().trim()

            if (!TextUtils.isEmpty(email) || password.isNotEmpty() || name.isNotEmpty())
                createAccount(email, password, name)
            else {
                Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_LONG).show()
            }
        }
    }
        private fun createAccount(email: String, password: String, displayName: String) {

            mAuth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener {
                    task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        var currUserId = mAuth?.currentUser
                        var userId = currUserId?.uid

                        // Create Table name Users in the database
                        mDatabase = FirebaseDatabase.getInstance()
                            .reference
                            .child("Users")
                            .child(userId.toString())

                        // Create a hashmap user with some data
                        var userObject = HashMap<String, String>()


                        userObject.put("display_name", displayName)
                        userObject.put("status", "Hello There...")
                        userObject.put("image", "default")
                        userObject.put("thumb_image", "default")

                        mDatabase?.setValue(userObject)?.addOnCompleteListener{
                            task: Task<Void> ->

                            if (task.isSuccessful) {
                                Toast.makeText(this, "Successful User created!", Toast.LENGTH_LONG).show()
                                val intent = DashboardActivity.getNewIntent(this).apply {
                                    putExtra("name", displayName)
                                }
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(this, "Fail User not created! ${task.exception}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }else {

                        Toast.makeText(this, "Fail User not created! ${task.exception}", Toast.LENGTH_LONG).show()
                    }
                }
        }
}