package com.example.mad1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(
    private val notificationList: ArrayList<Notification>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(notification: Notification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_notify, parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentItem = notificationList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val postDate: TextView = itemView.findViewById(R.id.tvPostDate)
        private val postTime: TextView = itemView.findViewById(R.id.tvPostTime)
        private val message: TextView = itemView.findViewById(R.id.tvMessage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(notification: Notification) {
            postDate.text = "Post Date: ${notification.postDate}"
            postTime.text = "Post Time: ${notification.postTime}"
            message.text = "Message: ${notification.message}"
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(notificationList[position])
            }
        }
    }
}
