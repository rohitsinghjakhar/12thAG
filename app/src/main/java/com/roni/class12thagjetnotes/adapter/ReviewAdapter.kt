package com.roni.class12thagjetnotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.models.Review

class ReviewAdapter(
    private val reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        private val tvStudentCourse: TextView = itemView.findViewById(R.id.tvStudentCourse)
        //private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        //private val tvReviewText: TextView = itemView.findViewById(R.id.tvReviewText)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(review: Review) {
            tvStudentName.text = review.studentName
            tvStudentCourse.text = review.studentCourse
            //ratingBar.rating = review.rating
            //tvReviewText.text = review.reviewText
            tvDate.text = review.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.size
}