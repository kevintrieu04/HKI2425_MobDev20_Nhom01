package com.example.cinemaapp.data

import com.google.firebase.Timestamp

data class Comment(
    val movieId: String = "",  // Default values to ensure it works even if Firestore data is missing some fields
    val userId: String = "",
    var userName: String = "",
    var profileImage: String = "",
    val commentText: String = "",
    val timestamp: Long = 0L
) {
    // Firebase yêu cầu constructor không có tham số
    constructor() : this("", "", "", "", "", 0L)
}