package com.example.mad1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotifymeActivity : AppCompatActivity(), NotificationAdapter.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var notificationRecyclerview: RecyclerView
    private lateinit var notificationArrayList: ArrayList<Notification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifyme)

        notificationRecyclerview = findViewById(R.id.notificationList)
        notificationRecyclerview.layoutManager = LinearLayoutManager(this)
        notificationRecyclerview.setHasFixedSize(true)

        notificationArrayList = arrayListOf()
        getNotificationData()
    }

    private fun getNotificationData() {
        Log.d("NotifymeActivity", "Fetching data from Firebase")
        dbref = FirebaseDatabase.getInstance().getReference("Notifications")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    notificationArrayList.clear()
                    for (notificationSnapshot in snapshot.children) {
                        val notification = notificationSnapshot.getValue(Notification::class.java)
                        if (notification != null && !notification.postDate.isNullOrEmpty() && !notification.postTime.isNullOrEmpty() && !notification.message.isNullOrEmpty()) {
                            notificationArrayList.add(notification)
                            Log.d("NotifyMeActivity", "Notification data added: $notification")
                        } else {
                            Log.e("NotifyMeActivity", "Invalid notification data or empty fields found: $notification")
                        }
                    }
                    notificationRecyclerview.adapter = NotificationAdapter(notificationArrayList, this@NotifymeActivity)
                } else {
                    Log.d("NotifyMeActivity", "No data found in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("NotifymeActivity", "Failed to read data from Firebase: ${error.message}")
            }
        })
    }

    override fun onItemClick(notification: Notification) {
        // Handle item click if needed
    }
}
