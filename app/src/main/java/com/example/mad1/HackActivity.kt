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

class HackActivity : AppCompatActivity(), HackAdapter.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var hackRecyclerview: RecyclerView
    private lateinit var hackArrayList: ArrayList<Hackathon>
    private lateinit var sharedPreferences: SharedPreferences  // SharedPreferences instance for storing registration status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hack)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("hackathon_prefs", Context.MODE_PRIVATE)

        hackRecyclerview = findViewById(R.id.hackList)
        hackRecyclerview.layoutManager = LinearLayoutManager(this)
        hackRecyclerview.setHasFixedSize(true)

        hackArrayList = arrayListOf()
        getHackData()
    }

    private fun getHackData() {
        Log.d("HackActivity", "Fetching data from Firebase")
        dbref = FirebaseDatabase.getInstance().getReference("Hackathons")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    hackArrayList.clear()
                    for (hackSnapshot in snapshot.children) {
                        val hack = hackSnapshot.getValue(Hackathon::class.java)
                        if (hack != null && !hack.eventName.isNullOrEmpty() && !hack.eventDate.isNullOrEmpty() && !hack.eventLocation.isNullOrEmpty() && !hack.eventLastDate.isNullOrEmpty() && !hack.eventURL.isNullOrEmpty()) {
                            // Retrieve registration status from SharedPreferences
                            val registered = sharedPreferences.getBoolean(hack.eventName, false)
                            hack.registered = registered
                            hackArrayList.add(hack)
                            Log.d("HackActivity", "Hackathon data added: $hack")
                        } else {
                            Log.e("HackActivity", "Invalid hackathon data or empty fields found: $hack")
                        }
                    }
                    hackRecyclerview.adapter = HackAdapter(hackArrayList, this@HackActivity, sharedPreferences)
                } else {
                    Log.d("HackActivity", "No data found in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HackActivity", "Failed to read data from Firebase: ${error.message}")
            }
        })
    }

    override fun onItemClick(hackathon: Hackathon) {
        val url = hackathon.eventURL
        if (url != "") {
            showConfirmationDialog(hackathon, url)
        } else {
            Log.e("HackActivity", "Empty URL for hackathon: $hackathon")
            // Handle error or notify user
        }
    }

    private fun showConfirmationDialog(hackathon: Hackathon, url: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you really want to register for this Hackathon?")
            .setPositiveButton("Yes") { dialog, id ->
                hackathon.registered = true
                // Save registration status in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putBoolean(hackathon.eventName, hackathon.registered)
                editor.apply()

                // Update the RecyclerView item
                hackRecyclerview.adapter?.notifyItemChanged(hackArrayList.indexOf(hackathon))

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
