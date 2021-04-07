package com.erikaosgue.mychatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.mychatapp.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {


    lateinit var  actCreateAccountBinding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actCreateAccountBinding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(actCreateAccountBinding.root)


    }
}