package com.erikaosgue.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.mychatapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class ChatActivity : AppCompatActivity() {

    var userId: String?=null
    var mFirebaseDatabaseRef: DatabaseReference?= null
    var mFirebaseUser: FirebaseUser?=null


    lateinit var actChatBinding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(actChatBinding.root)


    }
}