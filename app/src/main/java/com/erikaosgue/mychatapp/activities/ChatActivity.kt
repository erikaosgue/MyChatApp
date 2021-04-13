package com.erikaosgue.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.mychatapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    lateinit var actChatBinding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(actChatBinding.root)


    }
}