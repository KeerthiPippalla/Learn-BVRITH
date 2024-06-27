package com.example.mad1

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContestAdapter(
    private val contestList: ArrayList<Contest>,
    private val listener: OnItemClickListener,
    private val sharedPreferences: SharedPreferences  // SharedPreferences instance for storing registration status
) : RecyclerView.Adapter<ContestAdapter.ContestViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(contest: Contest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_contest, parent, false)
        return ContestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        val currentItem = contestList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return contestList.size
    }

    inner class ContestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val contestName: TextView = itemView.findViewById(R.id.tvContestName)
        private val platform: TextView = itemView.findViewById(R.id.tvPlatform)
        private val contestDate: TextView = itemView.findViewById(R.id.tvContestDate)
        private val time: TextView = itemView.findViewById(R.id.tvTime)
        private val duration: TextView = itemView.findViewById(R.id.tvDuration)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(contest: Contest) {
            contestName.text = "Contest Name: ${contest.contestName}"
            platform.text = "Platform: ${contest.platform}"
            contestDate.text = "Date: ${contest.contestDate}"
            time.text = "Time: ${contest.time}"
            duration.text = "Duration: ${contest.duration} hours"

            // Update UI based on registration status
            updateItemBackground(contest.registered)
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
                listener.onItemClick(contestList[position])
            }
        }
    }
}
