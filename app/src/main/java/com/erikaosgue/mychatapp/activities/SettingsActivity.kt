package com.erikaosgue.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.mychatapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {


    lateinit var actSettingsBinding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actSettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(actSettingsBinding.root)


    }
}