package com.example.mad1

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HackAdapter(
    private val hackList: ArrayList<Hackathon>,
    private val listener: OnItemClickListener,
    private val sharedPreferences: SharedPreferences  // SharedPreferences instance for storing registration status
) : RecyclerView.Adapter<HackAdapter.HackViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(hackathon: Hackathon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hackathon, parent, false)
        return HackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HackViewHolder, position: Int) {
        val currentItem = hackList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return hackList.size
    }

    inner class HackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val eventName: TextView = itemView.findViewById(R.id.tvEventName)
        private val eventDate: TextView = itemView.findViewById(R.id.tvEventDate)
        private val eventLocation: TextView = itemView.findViewById(R.id.tvEventLocation)
        private val eventLastDate: TextView = itemView.findViewById(R.id.tvEventLastDate)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(hackathon: Hackathon) {
            eventName.text = "Event Name: ${hackathon.eventName}"
            eventDate.text = "Event Date: ${hackathon.eventDate}"
            eventLocation.text = "Event Location: ${hackathon.eventLocation}"
            eventLastDate.text = "Last Date: ${hackathon.eventLastDate}"

            // Update UI based on registration status
            updateItemBackground(hackathon.registered)
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
                listener.onItemClick(hackList[position])
            }
        }
    }
}
