package com.erikaosgue.mychatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.erikaosgue.mychatapp.databinding.ActivitySettingsBinding
import com.erikaosgue.mychatapp.databinding.ActivityStatusBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StatusActivity : AppCompatActivity() {

    var mDatabase : DatabaseReference?= null
    var mCurrentUser: FirebaseUser? = null

    lateinit var actStatusBinding: ActivityStatusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actStatusBinding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(actStatusBinding.root)

        // Give a title to the Bar in the app
         supportActionBar?.title = "Status"

        checkOldStatus()
        setUpUI()

    }


    // Register the statusUpdate button to listen onclick
    private fun setUpUI() {


        actStatusBinding.statusUpdateBtn.setOnClickListener {

            // mCurrentUser is the current user logIn
            mCurrentUser = FirebaseAuth.getInstance().currentUser
            val userId = mCurrentUser?.uid

            // Get the read or write instance for the User base on userId
            mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId.toString())

            addNewUserStatus()


        }
    }

    //Add new user status into the database,
    // otherwise listen for an error
    private fun addNewUserStatus() {

        val status = actStatusBinding.statusUpdateEditText.text.toString().trim()

        // Set the new value of status and
        // listen if everything went perfect
        mDatabase?.child("status")
            ?.setValue(status)?.addOnCompleteListener { task: Task<Void> ->

                if (task.isSuccessful) {
                    Toast.makeText(this,
                        "Success changing status",
                        Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, SettingsActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this,
                        "Fail changing status: Error: ${task.exception}",
                        Toast.LENGTH_LONG).show()
                    Log.d("Fail add/change status:", task.exception.toString())
                }
            }
    }


    // Checks for an Old status of the user in the database
    // or Just display a message to create a new one
    private fun checkOldStatus() {

        if (intent.extras != null) {

            //oldStatus is what we got from the database
            val oldStatus = intent.extras!!.get("status")
            actStatusBinding.statusUpdateEditText.setText(oldStatus.toString())

        }
        else {
            actStatusBinding.statusUpdateEditText.setText("Enter your new status")
        }
    }
}