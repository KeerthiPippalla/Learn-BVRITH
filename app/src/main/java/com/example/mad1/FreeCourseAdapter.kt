package com.example.mad1

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FreeCourseAdapter(
    private val freeCourseList: ArrayList<FreeCourse>,
    private val listener: OnItemClickListener,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<FreeCourseAdapter.FreeCourseViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(freeCourse: FreeCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeCourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_freecourse, parent, false)
        return FreeCourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FreeCourseViewHolder, position: Int) {
        val currentItem = freeCourseList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return freeCourseList.size
    }

    inner class FreeCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val courseName: TextView = itemView.findViewById(R.id.tvCourseName)
        private val courseDuration: TextView = itemView.findViewById(R.id.tvCourseDuration)
        private val courseProvider: TextView = itemView.findViewById(R.id.tvCourseProvider)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(freeCourse: FreeCourse) {
            courseName.text = "Course Name: ${freeCourse.courseName}"
            courseDuration.text = "Duration: ${freeCourse.courseDuration}"
            courseProvider.text = "Provided by: ${freeCourse.courseProvider}"

            // Update UI based on registration status
            updateItemBackground(freeCourse.registered)
        }

        private fun updateItemBackground(isRegistered: Boolean) {
            if (isRegistered) {
                itemView.setBackgroundResource(R.color.grey)
            } else {
                itemView.setBackgroundResource(android.R.color.white)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Trigger the onItemClick listener without changing registration status
                listener.onItemClick(freeCourseList[position])
            }
        }
    }
}
