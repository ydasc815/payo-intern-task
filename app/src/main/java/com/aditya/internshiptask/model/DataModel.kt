package com.aditya.internshiptask.model

import com.squareup.moshi.Json

data class APIModel(
    val page: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "data") val records: List<RecordsData>
)

data class RecordsData(
    val email: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "avatar") val imgUrl: String
)