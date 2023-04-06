package com.example.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _dogLiveData = MutableLiveData<DogBreed>()
    val dogLiveData: LiveData<DogBreed>
        get() = _dogLiveData

    fun getDogInformation(id: Int) {
        val dao = DogDatabase(getApplication()).dogDao()
        viewModelScope.launch {
            _dogLiveData.postValue(dao.getDog(id))
        }
    }
}