package com.example.dogs.repository

import com.example.dogs.api.RetrofitInstance
import com.example.dogs.model.DogBreed

class Repository {

    suspend fun getDogs(): List<DogBreed>{
        return RetrofitInstance.api.getDogs()
    }
}