package com.example.imagedisplayerproject

import retrofit2.http.GET

interface ApiService {
    @GET("games")
    suspend fun getGames() : List<Games>
}