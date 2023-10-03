package com.example.imagedisplayerproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import coil.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Note that this class is extending a view model because the class will make use of ui related things and then to make it survive things like screen rotation and all
class Retrofit(application: Application) : AndroidViewModel(application){

    // To get the api Service
    private val apiService = Retrofit.Builder()
        .baseUrl("https://www.freetogame.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    // To create the image loader for Coil
    private val imageLoader = ImageLoader.Builder(application)
        .crossfade(true) // This is a kind of animation when an image is replacing another
        .build()
    init {
        Coil.setImageLoader(imageLoader)
        fetGames()
    }
    // Note
    /*
    init Block: In Kotlin, the 'init' block is a special block that is executed when an
    instance of the class is created. It's used for executing code that initializes the object or
    performs other setup tasks. In this case, the init block is part of the class constructor
    and runs as soon as an instance of the class is created.
     */

    // Since the data is changing we want to store it in a mutable state flow
    private val _games = MutableStateFlow<List<Games>>(emptyList()) // It has to be initialized as an empty list

    // Then to create a state flow that is immutable
    val games: StateFlow<List<Games>> = _games

    init {
        fetGames()
    }

    fun fetGames(){

        viewModelScope.launch (Dispatchers.IO) {
            try{
                val response = apiService.getGames()
                _games.value = response

            } catch (e:Exception){

            }
        }
    }

}