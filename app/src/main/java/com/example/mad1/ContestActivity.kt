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

class ContestActivity : AppCompatActivity(), ContestAdapter.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var contestRecyclerView: RecyclerView
    private lateinit var contestArrayList: ArrayList<Contest>
    private lateinit var sharedPreferences: SharedPreferences  // SharedPreferences instance for storing registration status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contest)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("contest_prefs", Context.MODE_PRIVATE)

        contestRecyclerView = findViewById(R.id.contestList)
        contestRecyclerView.layoutManager = LinearLayoutManager(this)
        contestRecyclerView.setHasFixedSize(true)

        contestArrayList = arrayListOf()
        getContestData()
    }

    private fun getContestData() {
        Log.d("ContestActivity", "Fetching data from Firebase")
        dbref = FirebaseDatabase.getInstance().getReference("Contests")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    contestArrayList.clear()
                    for (contestSnapshot in snapshot.children) {
                        val contest = contestSnapshot.getValue(Contest::class.java)
                        if (contest != null && !contest.contestName.isNullOrEmpty() && !contest.platform.isNullOrEmpty() && !contest.contestDate.isNullOrEmpty() && !contest.time.isNullOrEmpty() && !contest.duration.isNullOrEmpty() && !contest.contestUrl.isNullOrEmpty()) {
                            // Retrieve registration status from SharedPreferences
                            val registered = sharedPreferences.getBoolean(contest.contestName, false)
                            contest.registered = registered
                            contestArrayList.add(contest)
                            Log.d("ContestActivity", "Contest data added: $contest")
                        } else {
                            Log.e("ContestActivity", "Invalid contest data or empty fields found: $contest")
                        }
                    }
                    contestRecyclerView.adapter = ContestAdapter(contestArrayList, this@ContestActivity, sharedPreferences)
                } else {
                    Log.d("ContestActivity", "No data found in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ContestActivity", "Failed to read data from Firebase: ${error.message}")
            }
        })
    }

    override fun onItemClick(contest: Contest) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you really want to register for the contest?")
            .setPositiveButton("Yes") { dialog, id ->
                val url = contest.contestUrl
                if (url!="") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    contest.registered = true
                    saveRegistrationStatus(contest)
                    contestRecyclerView.adapter?.notifyItemChanged(contestArrayList.indexOf(contest))
                } else {
                    Log.e("ContestActivity", "Empty URL for contest: $contest")
                }
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun saveRegistrationStatus(contest: Contest) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(contest.contestName, contest.registered)
        editor.apply()
    }
}
