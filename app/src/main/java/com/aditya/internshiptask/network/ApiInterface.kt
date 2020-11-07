package com.aditya.internshiptask.network

import com.aditya.internshiptask.model.APIModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://reqres.in/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RecordsApiService {
    @GET("/api/users")
    fun getRecords(
        @Query("page") page: Int
    ): Call<APIModel>
}

object RecordsApi {
    val retrofitService: RecordsApiService by lazy {
        retrofit.create(RecordsApiService::class.java)
    }
}