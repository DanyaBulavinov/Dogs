package com.example.dogs.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogDatabase
import com.example.dogs.repository.Repository
import com.example.dogs.utils.Resource
import com.example.dogs.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class ListViewModel(application: Application, private val repository: Repository) :
    AndroidViewModel(application) {

//    private val dogsService = Repository()
//    private val disposable = CompositeDisposable()

//    private val _dogs = MutableLiveData<List<DogBreed>>()
//    val dogs: LiveData<List<DogBreed>>
//        get() = _dogs
//    private val _dogsLoadError = MutableLiveData<Boolean>()
//    val dogsLoadError: LiveData<Boolean>
//        get() = _dogsLoadError
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean>
//        get() = _loading


//    fun getDogs(){
//        viewModelScope.launch {
//            val response = repository.getDogs()
//            _dogs.value = response
//        }
//    }

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private val _dogsList = MutableLiveData<Resource<List<DogBreed>>>()
    val dogsList: LiveData<Resource<List<DogBreed>>>
        get() = _dogsList

//    fun getDogs() = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//        emit(Resource.success(data = repository.getDogs()))
//        } catch (exception: Exception){
//        emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
//        }
//    }

    fun getDogs() {
        _dogsList.value = Resource.loading(data = null)
        viewModelScope.launch {
            try {
                _dogsList.postValue(Resource.success(data = repository.getDogs()))
            } catch (exception: Exception) {
                _dogsList.postValue(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred!"
                    )
                )
            }
        }
    }

    fun storeDogsLocally(dogs: List<DogBreed>) {
        viewModelScope.launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*dogs.toTypedArray())
            var i = 0
            while (i < dogs.size) {
                dogs[i].uuid = result[i].toInt()
                Log.d("DOG_ID", dogs.size.toString())
//                Log.d("DOG_ID", i.toString())
                ++i
            }
            _dogsList.value = Resource.success(dogs)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }


//    suspend fun refresh() {
//        fetchFromRemote()
//    }

//    private fun fetchFromRemote(){
//          _dogs.value = dogsService.getDogs()
//    }

//    private suspend fun fetchFromRemote() {
//        _loading.postValue(true)
//        disposable.add(
//            dogsService.getDogs()
//                .subscribeOn(Schedulers.newThread())
//                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
//                    override fun onSuccess(dogList: List<DogBreed>?) {
//                        _dogs.postValue(dogList ?: throw RuntimeException("dogList == null"))
//                        _dogsLoadError.postValue(false)
//                        _loading.postValue(false)
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        _dogsLoadError.postValue(true)
//                        _loading.postValue(false)
//                        e?.printStackTrace()
//                    }
//                })
//        )
//    }


//    override fun onCleared() {
//        super.onCleared()
//        disposable.clear()
//    }
}





















