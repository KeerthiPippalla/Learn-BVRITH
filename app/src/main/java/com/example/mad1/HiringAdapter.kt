package com.example.mad1

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HiringAdapter(
    private val hiringList: ArrayList<HiringChallenge>,
    private val listener: OnItemClickListener,
    private val sharedPreferences: SharedPreferences // SharedPreferences instance for storing registration status
) : RecyclerView.Adapter<HiringAdapter.HiringViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(hiringChallenge: HiringChallenge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiringViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hiringchallenges, parent, false)
        return HiringViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HiringViewHolder, position: Int) {
        val currentItem = hiringList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return hiringList.size
    }

    inner class HiringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val challengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
        private val eligibility: TextView = itemView.findViewById(R.id.tvEligibility)
        private val registerDeadline: TextView = itemView.findViewById(R.id.tvRegisterDeadline)
        private val firstPhaseDate: TextView = itemView.findViewById(R.id.tvFirstPhaseDate)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(hiringChallenge: HiringChallenge) {
            challengeName.text = "Challenge Name: ${hiringChallenge.challengeName}"
            eligibility.text = "Eligibility: ${hiringChallenge.eligibility}"
            registerDeadline.text = "Register Deadline: ${hiringChallenge.registerDeadline}"
            firstPhaseDate.text = "1st Phase Date: ${hiringChallenge.firstPhaseDate}"

            // Update UI based on registration status
            updateItemBackground(hiringChallenge.registered)
        }

        private fun updateItemBackground(isRegistered: Boolean) {
            if (isRegistered) {
                // Handle UI for registered state
                itemView.setBackgroundResource(R.color.grey)
            } else {
                // Handle UI for unregistered state
                itemView.setBackgroundResource(android.R.color.white)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Trigger the onItemClick listener without changing registration status
                listener.onItemClick(hiringList[position])
            }
        }
    }
}
