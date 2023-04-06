package com.example.dogs.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogDatabase
import com.example.dogs.repository.Repository
import com.example.dogs.utils.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(application: Application, private val repository: Repository) :
    AndroidViewModel(application) {


    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val _dogs = MutableLiveData<List<DogBreed>>()
    val dogs: LiveData<List<DogBreed>>
        get() = _dogs
    private val _dogsLoadError = MutableLiveData<Boolean>()
    val dogsLoadError: LiveData<Boolean>
        get() = _dogsLoadError
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


    fun refresh() {
        val updateTime = prefHelper.getUpdateTime()
        Log.d("UPDATE_TIME", updateTime.toString())
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDogsFromDatabase()
        } else {
            getDogsFromRemote()
        }
    }

    fun refreshBypassCache() {
        getDogsFromRemote()
    }

    private fun getDogsFromDatabase() {
        _loading.value = true
        viewModelScope.launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getDogsFromRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            try {
                val response = repository.getDogs()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        storeDogsLocally(response.body())
                        Toast.makeText(
                            getApplication(),
                            "Dogs retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onError()
                    }
                }
            } catch (exception: Exception) {
                onError()
            }
        }
    }

    private fun dogsRetrieved(dogsList: List<DogBreed>?) {
        _dogs.postValue(dogsList ?: throw RuntimeException("dogsList == null"))
        _loading.postValue(false)
        _dogsLoadError.postValue(false)
    }

    private fun onError() {
        _dogsLoadError.postValue(true)
        _loading.postValue(false)
    }

    private fun storeDogsLocally(dogsList: List<DogBreed>?) {
        val list = dogsList ?: throw RuntimeException("dogsList == null")
        viewModelScope.launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val insertedIds = dao.insertAll(*list.toTypedArray())
            val retrievedDogs = dao.getAllDogs()
            retrievedDogs.forEachIndexed { index, dog -> list[index].uuid = dog.uuid  }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }
}





















