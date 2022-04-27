package com.erdiansyah.mystory.presenter

import androidx.lifecycle.*
import com.erdiansyah.mystory.data.UserRepository
import com.erdiansyah.mystory.data.remote.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.erdiansyah.mystory.data.Result
import com.erdiansyah.mystory.data.remote.LoginResult
import com.erdiansyah.mystory.data.remote.RegistResponse

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginResponseData = MutableLiveData<Result<LoginResponse>>()
    val loginResponseData: LiveData<Result<LoginResponse>> = _loginResponseData

    private val _registResponseData = MutableLiveData<Result<RegistResponse>>()
    val registResponseData: LiveData<Result<RegistResponse>> = _registResponseData

    fun loginUser(email: String, password: String) {
        _loginResponseData.value = Result.Loading()
        viewModelScope.launch {
            _loginResponseData.value = repository.loginUser(email, password)
        }
    }

    fun getSession(): LiveData<LoginResult?> {
        return repository.getSession().asLiveData()
    }

    fun logoutUser(){
        viewModelScope.launch {
            return@launch repository.logout()
        }
    }

    fun resetRegistForm() {
        _registResponseData.value = Result.NoState()
    }

    fun registUser(name: String, email: String, password: String) {
        _registResponseData.value = Result.Loading()
        viewModelScope.launch {
            _registResponseData.value = repository.registUser(name, email, password)
        }
    }
}