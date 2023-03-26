package com.example.dogs.repository

import com.example.dogs.api.RetrofitInstance
import com.example.dogs.model.DogBreed
import retrofit2.Response

class Repository {

    suspend fun getDogs(): Response<List<DogBreed>> {
        return RetrofitInstance.api.getDogs()
    }
}