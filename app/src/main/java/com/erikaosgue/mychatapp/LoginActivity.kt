package com.erikaosgue.mychatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.mychatapp.databinding.ActivityLoginBinding
import com.erikaosgue.mychatapp.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {


    lateinit var  actLoginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(actLoginBinding.root)


    }
}