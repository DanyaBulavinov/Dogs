package com.example.dogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed

class DetailViewModel : ViewModel() {

    private val _dogLiveData = MutableLiveData<DogBreed>()
    val dogLiveData: LiveData<DogBreed>
        get() = _dogLiveData

    fun fetch(){
        val dog = DogBreed("1", "Corgi", "15 years", "breedGroup", "breedFor", "temperament", "URL")
        _dogLiveData.value = dog
    }

}