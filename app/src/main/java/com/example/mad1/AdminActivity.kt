package com.example.mad1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private lateinit var eventNameEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var deadlineEditText: EditText
    private lateinit var urlEditText1: EditText
    private lateinit var insertHackathonButton: Button

    private lateinit var challengeNameEditText: EditText
    private lateinit var eligibilityEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var firstPhaseEditText: EditText
    private lateinit var urlEditText3: EditText
    private lateinit var insertChallengeButton: Button

    private lateinit var courseNameEditText: EditText
    private lateinit var courseDurationEditText: EditText
    private lateinit var courseProviderEditText: EditText
    private lateinit var urlEditText2: EditText
    private lateinit var insertCourseButton: Button

    private lateinit var contestNameEditText: EditText
    private lateinit var platformEditText: EditText
    private lateinit var contestDateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var contestUrlEditText: EditText
    private lateinit var insertContestButton: Button

    private lateinit var postDateEditText: EditText
    private lateinit var postTimeEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var insertNotificationButton: Button

    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        database = FirebaseDatabase.getInstance().reference

        eventNameEditText = findViewById(R.id.eventNameEditText)
        locationEditText = findViewById(R.id.locationEditText)
        dateEditText = findViewById(R.id.dateEditText)
        deadlineEditText = findViewById(R.id.deadlineEditText)
        urlEditText1 = findViewById(R.id.urlEditText1)
        insertHackathonButton = findViewById(R.id.insertHackathonButton)

        challengeNameEditText = findViewById(R.id.challengeNameEditText)
        eligibilityEditText = findViewById(R.id.eligibilityEditText)
        endDateEditText = findViewById(R.id.endDateEditText)
        firstPhaseEditText = findViewById(R.id.firstPhaseEditText)
        urlEditText3 = findViewById(R.id.urlEditText3)
        insertChallengeButton = findViewById(R.id.insertChallengeButton)

        courseNameEditText = findViewById(R.id.courseNameEditText)
        courseDurationEditText = findViewById(R.id.courseDurationEditText)
        courseProviderEditText = findViewById(R.id.courseProviderEditText)
        urlEditText2 = findViewById(R.id.urlEditText2)
        insertCourseButton = findViewById(R.id.insertCourseButton)

        contestNameEditText = findViewById(R.id.contestNameEditText)
        platformEditText = findViewById(R.id.platformEditText)
        contestDateEditText = findViewById(R.id.contestDateEditText)
        timeEditText = findViewById(R.id.timeEditText)
        durationEditText = findViewById(R.id.durationEditText)
        contestUrlEditText = findViewById(R.id.contestUrlEditText)
        insertContestButton = findViewById(R.id.insertContestButton)

        postDateEditText = findViewById(R.id.postDateEditText)
        postTimeEditText = findViewById(R.id.postTimeEditText)
        messageEditText = findViewById(R.id.messageEditText)
        insertNotificationButton = findViewById(R.id.insertNotificationButton)

        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logoutUser()
        }

        insertHackathonButton.setOnClickListener {
            val eventName = eventNameEditText.text.toString()
            val location = locationEditText.text.toString()
            val date = dateEditText.text.toString()
            val deadline = deadlineEditText.text.toString()
            val url = urlEditText1.text.toString()

            if (eventName.isNotEmpty()) {
                val hackathon = Hackathon(eventName, location, date, deadline, url, false)

                database.child("Hackathons").child(eventName).setValue(hackathon).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Hackathon inserted successfully")
                        eventNameEditText.text.clear()
                        locationEditText.text.clear()
                        dateEditText.text.clear()
                        deadlineEditText.text.clear()
                        urlEditText1.text.clear()
                    } else {
                        showToast("Failed to insert hackathon")
                    }
                }
            } else {
                showToast("Event name cannot be empty")
            }
        }


        insertChallengeButton.setOnClickListener {
            val challengeName = challengeNameEditText.text.toString()
            val eligibility = eligibilityEditText.text.toString()
            val endDate = endDateEditText.text.toString()
            val firstPhaseDate = firstPhaseEditText.text.toString()
            val url = urlEditText3.text.toString()

            if (challengeName.isNotEmpty()) {
                val hiringChallenge = HiringChallenge(challengeName, eligibility, endDate, firstPhaseDate, url)

                database.child("HiringChallenges").child(challengeName).setValue(hiringChallenge).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Hiring challenge inserted successfully")
                        challengeNameEditText.text.clear()
                        eligibilityEditText.text.clear()
                        endDateEditText.text.clear()
                        firstPhaseEditText.text.clear()
                        urlEditText3.text.clear()
                    } else {
                        showToast("Failed to insert hiring challenge")
                    }
                }
            } else {
                showToast("Challenge name cannot be empty")
            }
        }

        insertCourseButton.setOnClickListener {
            val courseName = courseNameEditText.text.toString()
            val courseDuration = courseDurationEditText.text.toString()
            val courseProvider = courseProviderEditText.text.toString()
            val url = urlEditText2.text.toString()

            if (courseName.isNotEmpty()) {
                val freeCourse = FreeCourse(courseName, courseDuration, courseProvider, url)

                database.child("FreeCourses").child(courseName).setValue(freeCourse).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        courseNameEditText.text.clear()
                        courseDurationEditText.text.clear()
                        courseProviderEditText.text.clear()
                        urlEditText2.text.clear()
                        showToast("Free course inserted successfully")
                    } else {
                        showToast("Failed to insert free course")
                    }
                }
            } else {
                showToast("Course name cannot be empty")
            }
        }

        insertContestButton.setOnClickListener {
            val contestName = contestNameEditText.text.toString()
            val platform = platformEditText.text.toString()
            val contestDate = contestDateEditText.text.toString()
            val time = timeEditText.text.toString()
            val duration = durationEditText.text.toString()
            val contestUrl = contestUrlEditText.text.toString()

            if (contestName.isNotEmpty()) {
                val contest = Contest(contestName, platform, contestDate, time, duration, contestUrl)

                database.child("Contests").child(contestName).setValue(contest).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Contest inserted successfully")
                        contestNameEditText.text.clear()
                        platformEditText.text.clear()
                        contestDateEditText.text.clear()
                        timeEditText.text.clear()
                        durationEditText.text.clear()
                        contestUrlEditText.text.clear()
                    } else {
                        showToast("Failed to insert contest")
                    }
                }
            } else {
                showToast("Contest name cannot be empty")
            }
        }

        insertNotificationButton.setOnClickListener {
            val postDate = postDateEditText.text.toString()
            val postTime = postTimeEditText.text.toString()
            val message = messageEditText.text.toString()

            if (message.isNotEmpty()) {
                val notification = Notification(postDate, postTime, message)

                database.child("Notifications").push().setValue(notification).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        postDateEditText.text.clear()
                        postTimeEditText.text.clear()
                        messageEditText.text.clear()
                        showToast("Notification sent successfully")
                    } else {
                        showToast("Failed to send notification")
                    }
                }
            } else {
                showToast("Message cannot be empty")
            }
        }
    }

    private fun logoutUser() {
        // Clear any user session data if necessary
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
