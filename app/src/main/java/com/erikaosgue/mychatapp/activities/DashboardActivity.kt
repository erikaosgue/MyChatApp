package com.erikaosgue.mychatapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erikaosgue.mychatapp.databinding.ActivityCreateAccountBinding
import com.erikaosgue.mychatapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    companion object {
        fun getNewIntent(context: Context) = Intent(context, DashboardActivity::class.java)

    }

    lateinit var  actDashboardBinding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actDashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(actDashboardBinding.root)


        val extras = intent.extras
        if (extras != null) {
            val userName = extras.get("name")
            Toast.makeText(this, "Username: $userName", Toast.LENGTH_LONG).show()

        }
    }
}