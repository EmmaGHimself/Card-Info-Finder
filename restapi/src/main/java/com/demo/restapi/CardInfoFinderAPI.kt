package com.demo.restapi

import retrofit2.http.GET
import com.demo.restapi.models.CardInfoDetails
import retrofit2.Call
import retrofit2.http.Path

interface CardInfoFinderAPI {
    @GET("{bin}")
    fun getCardInfoDetails(@Path("bin") bin: String?): Call<CardInfoDetails?>?
}