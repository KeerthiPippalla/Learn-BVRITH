package com.example.mad1

import android.content.Context
import android.content.SharedPreferences
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
import android.content.Intent
import android.net.Uri


class FreeCoursesActivity : AppCompatActivity(), FreeCourseAdapter.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var freeCourseRecyclerView: RecyclerView
    private lateinit var freeCourseArrayList: ArrayList<FreeCourse>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_courses)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("freecourse_prefs", Context.MODE_PRIVATE)

        freeCourseRecyclerView = findViewById(R.id.freeCourseList)
        freeCourseRecyclerView.layoutManager = LinearLayoutManager(this)
        freeCourseRecyclerView.setHasFixedSize(true)

        freeCourseArrayList = arrayListOf()
        getFreeCourseData()
    }

    private fun getFreeCourseData() {
        Log.d("FreeCoursesActivity", "Fetching data from Firebase")
        dbref = FirebaseDatabase.getInstance().getReference("FreeCourses")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    freeCourseArrayList.clear()
                    for (courseSnapshot in snapshot.children) {
                        val course = courseSnapshot.getValue(FreeCourse::class.java)
                        if (course != null &&
                            !course.courseName.isNullOrEmpty() &&
                            !course.courseDuration.isNullOrEmpty() &&
                            !course.courseProvider.isNullOrEmpty() &&
                            !course.courseURL.isNullOrEmpty()
                        ) {
                            // Retrieve registration status from SharedPreferences
                            val registered = sharedPreferences.getBoolean(course.courseName, false)
                            course.registered = registered
                            freeCourseArrayList.add(course)
                            Log.d("FreeCoursesActivity", "Free course data added: $course")
                        } else {
                            Log.e("FreeCoursesActivity", "Invalid free course data or empty fields found: $course")
                        }
                    }
                    freeCourseRecyclerView.adapter = FreeCourseAdapter(freeCourseArrayList, this@FreeCoursesActivity, sharedPreferences)
                } else {
                    Log.d("FreeCoursesActivity", "No data found in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FreeCoursesActivity", "Failed to read data from Firebase: ${error.message}")
            }
        })
    }

    override fun onItemClick(freeCourse: FreeCourse) {
        showConfirmationDialog(freeCourse)
    }

    private fun showConfirmationDialog(freeCourse: FreeCourse) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you really want to register for this course?")
            .setPositiveButton("Yes") { dialog, id ->
                freeCourse.registered = true
                // Save registration status in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putBoolean(freeCourse.courseName, freeCourse.registered)
                editor.apply()

                // Notify adapter of the registration change
                freeCourseRecyclerView.adapter?.notifyDataSetChanged()

                // Redirect to course URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(freeCourse.courseURL)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}
