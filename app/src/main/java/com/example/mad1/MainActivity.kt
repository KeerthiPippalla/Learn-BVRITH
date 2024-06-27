package com.example.mad1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mad1.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    companion object{
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.HackathonCard.setOnClickListener {
            openHackActivity()
        }
        binding.CourseCard.setOnClickListener{
            openFreeCoursesActivity()
        }
        binding.ContestCard.setOnClickListener{
            openContest()
        }
        binding.HiringCard.setOnClickListener{
            openHiringChallengesActivity()
        }
        binding.NotifyCard.setOnClickListener {
            openNotifymeActivity()
        }
        binding.LogoutCard.setOnClickListener {
            onlogout()
        }
        FirebaseApp.initializeApp(this)
    }


    private fun openHackActivity() {
        val intent = Intent(this, HackActivity::class.java)
        startActivity(intent)
    }
    private fun openFreeCoursesActivity(){
        val intent=Intent(this,FreeCoursesActivity::class.java)
        startActivity(intent)
    }
    private fun openHiringChallengesActivity(){
        val intent=Intent(this,HiringChallengesActivity::class.java)
        startActivity(intent)
    }
    private fun openContest(){
        val intent = Intent(this, ContestActivity::class.java)
        startActivity(intent)

    }
    private fun openNotifymeActivity(){
        val intent=Intent(this,NotifymeActivity::class.java)
        startActivity(intent)
    }
    private fun onlogout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent the user from going back to it
    }
}