package com.erdiansyah.mystory.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.erdiansyah.mystory.data.StoryRepository
import kotlinx.coroutines.launch
import com.erdiansyah.mystory.data.Result
import com.erdiansyah.mystory.data.remote.PostStoryResponse
import com.erdiansyah.mystory.data.remote.StoryLocationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {
    //private val _getStoryResponse = MutableLiveData<Pager<Int, ListStoryItem>>()
    val getStoryResponse = repository.getStory().cachedIn(viewModelScope)

    private val _postStoryResponse = MutableLiveData<Result<PostStoryResponse>>()
    val postStoryResponse : LiveData<Result<PostStoryResponse>> = _postStoryResponse

    private val _getStoryLocationResponse = MutableLiveData<Result<StoryLocationResponse>>()
    val getStoryLocationResponse: LiveData<Result<StoryLocationResponse>> = _getStoryLocationResponse

//    fun getStory() {
//        viewModelScope.launch {
//            _getStoryResponse.postValue(repository.getStory())
//        }
//    }

    fun resetForm() {
        _postStoryResponse.value = Result.NoState()
    }

    fun postStory(image: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?) {
        _postStoryResponse.value = Result.Loading()
        viewModelScope.launch {
            _postStoryResponse.postValue(repository.postStory(image, description, lat, lon))
        }
    }

    fun getStoryLocation() {
        _getStoryLocationResponse.value = Result.Loading()
        viewModelScope.launch {
            _getStoryLocationResponse.postValue(repository.getStoryLocation())
        }
    }
}