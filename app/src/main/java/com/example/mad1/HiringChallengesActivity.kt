package com.example.mad1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HiringChallengesActivity : AppCompatActivity(), HiringAdapter.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var hiringRecyclerview: RecyclerView
    private lateinit var hiringArrayList: ArrayList<HiringChallenge>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiring_challenges)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("hiring_prefs", Context.MODE_PRIVATE)

        hiringRecyclerview = findViewById(R.id.hiringList)
        hiringRecyclerview.layoutManager = LinearLayoutManager(this)
        hiringRecyclerview.setHasFixedSize(true)

        hiringArrayList = arrayListOf()
        getHiringData()
    }

    private fun getHiringData() {
        Log.d("HiringActivity", "Fetching data from Firebase")
        dbref = FirebaseDatabase.getInstance().getReference("HiringChallenges")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    hiringArrayList.clear()
                    for (hiringSnapshot in snapshot.children) {
                        val hiring = hiringSnapshot.getValue(HiringChallenge::class.java)
                        if (hiring != null && !hiring.challengeName.isNullOrEmpty() && !hiring.eligibility.isNullOrEmpty() && !hiring.registerDeadline.isNullOrEmpty() && !hiring.firstPhaseDate.isNullOrEmpty() && !hiring.url.isNullOrEmpty()) {
                            // Retrieve registration status from SharedPreferences
                            val registered = sharedPreferences.getBoolean(hiring.challengeName, false)
                            hiring.registered = registered
                            hiringArrayList.add(hiring)
                            Log.d("HiringActivity", "HiringChallenge data added: $hiring")
                        } else {
                            Log.e("HiringActivity", "Invalid hiring challenge data or empty fields found: $hiring")
                        }
                    }
                    hiringRecyclerview.adapter = HiringAdapter(hiringArrayList, this@HiringChallengesActivity, sharedPreferences)
                } else {
                    Log.d("HiringActivity", "No data found in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HiringActivity", "Failed to read data from Firebase: ${error.message}")
            }
        })
    }

    override fun onItemClick(hiringChallenge: HiringChallenge) {
        val url = hiringChallenge.url
        if (url != "") {
            showConfirmationDialog(hiringChallenge, url)
        } else {
            Log.e("HiringActivity", "Empty URL for hiring challenge: $hiringChallenge")
            // Handle error or notify user
        }
    }

    private fun showConfirmationDialog(hiringChallenge: HiringChallenge, url: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you really want to register for this Hiring Challenge?")
            .setPositiveButton("Yes") { dialog, id ->
                hiringChallenge.registered = true
                // Save registration status in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putBoolean(hiringChallenge.challengeName, hiringChallenge.registered)
                editor.apply()

                // Update the RecyclerView item
                hiringRecyclerview.adapter?.notifyItemChanged(hiringArrayList.indexOf(hiringChallenge))

                // Redirect to URL
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}
