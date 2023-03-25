package com.example.dogs.api

import com.example.dogs.model.DogBreed
import retrofit2.http.GET

interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    suspend fun getDogs(): List<DogBreed>
}