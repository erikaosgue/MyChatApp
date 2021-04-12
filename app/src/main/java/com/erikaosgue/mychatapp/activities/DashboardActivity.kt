package com.erikaosgue.mychatapp.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.adapters.SectionPagerAdapter
import com.erikaosgue.mychatapp.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private  var sectionAdapter: SectionPagerAdapter? = null
    companion object {
        fun getNewIntent(context: Context) = Intent(context, DashboardActivity::class.java)

    }

    lateinit var  actDashboardBinding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actDashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(actDashboardBinding.root)
        // Adds the title bar
        supportActionBar?.title = "Dashboard"

        sectionAdapter = SectionPagerAdapter(supportFragmentManager)
        actDashboardBinding.dashViewPaperId.adapter = sectionAdapter
        actDashboardBinding.mainTabs.setupWithViewPager(actDashboardBinding.dashViewPaperId)
        actDashboardBinding.mainTabs.setTabTextColors(Color.GRAY, Color.WHITE)


//        val extras = intent.extras
//        if (extras != null) {
//            val userName = extras.get("name")
//            Toast.makeText(this, "Username: $userName", Toast.LENGTH_LONG).show()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item != null && item.itemId == R.id.logoutId) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
        // Log the user out
        if (item.itemId == R.id.settingsId) {
                //Take user to settingsActivity
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return true

    }


}